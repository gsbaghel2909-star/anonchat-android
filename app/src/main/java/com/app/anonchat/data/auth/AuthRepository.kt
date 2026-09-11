package com.app.anonchat.data.auth

import com.app.anonchat.domain.model.AuthErrorCode
import com.app.anonchat.domain.model.AuthResult
import com.app.anonchat.domain.model.LoginRequest
import com.app.anonchat.domain.model.SignUpRequest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Serializable
private data class CompleteSignupBody(val dateOfBirth: String)

@Singleton
class AuthRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val functionsHttpClient: HttpClient,
    @Named("edgeFunctionsBaseUrl") private val edgeFunctionsBaseUrl: String
) {

    /**
     * Two-step signup:
     *  1. Create the credential via Supabase Auth (email/password).
     *  2. Call the `complete-signup` Edge Function, which independently
     *     re-validates age server-side and creates the app_auth.accounts
     *     row. Step 2's age check is authoritative — step 1 succeeding does
     *     NOT mean the account is usable yet.
     */
    suspend fun signUp(request: SignUpRequest): AuthResult<Unit> {
        if (!request.meetsMinimumAge()) {
            // Fail fast client-side for UX, but this is never the only gate.
            return AuthResult.Failure("You must be 18 or older to use this app.", AuthErrorCode.UNDER_AGE)
        }

        return try {
            supabase.auth.signUpWith(Email) {
                email = request.email
                password = request.password
            }

            val session = supabase.auth.currentSessionOrNull()
                ?: return AuthResult.Failure("Session not established after sign up.", AuthErrorCode.UNKNOWN)

            val response = functionsHttpClient.post("$edgeFunctionsBaseUrl/complete-signup") {
                header("Authorization", "Bearer ${session.accessToken}")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(
                    CompleteSignupBody.serializer(),
                    CompleteSignupBody(dateOfBirth = request.dateOfBirth.toString())
                ))
            }

            if (response.status.value == 403) {
                // Server rejected on age grounds even though client check passed
                // (e.g. clock manipulation) — sign the (unusable) auth record back out.
                supabase.auth.signOut()
                return AuthResult.Failure("You must be 18 or older to use this app.", AuthErrorCode.UNDER_AGE)
            }
            if (response.status.value != 200) {
                return AuthResult.Failure("Could not complete sign up. Please try again.", AuthErrorCode.UNKNOWN)
            }

            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure(e.message ?: "Network error", AuthErrorCode.NETWORK_ERROR)
        }
    }

    suspend fun login(request: LoginRequest): AuthResult<Unit> {
        return try {
            supabase.auth.signInWith(Email) {
                email = request.email
                password = request.password
            }
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure("Invalid email or password.", AuthErrorCode.INVALID_CREDENTIALS)
        }
    }

    suspend fun logout() {
        supabase.auth.signOut()
    }

    fun isLoggedIn(): Boolean = supabase.auth.currentSessionOrNull() != null
}
