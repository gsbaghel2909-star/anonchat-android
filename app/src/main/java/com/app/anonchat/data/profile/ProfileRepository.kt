package com.app.anonchat.data.profile

import com.app.anonchat.domain.model.AuthErrorCode
import com.app.anonchat.domain.model.AuthResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class ProfileRow(
    val id: String,
    val username: String,
    val display_name: String? = null
)

@Singleton
class ProfileRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {

    suspend fun getCurrentProfile(): ProfileRow? {
        val userId = supabaseClient.auth.currentUserOrNull()?.id ?: return null
        return supabaseClient.postgrest["profiles"]
            .select(columns = Columns.ALL) {
                filter { eq("id", userId) }
            }
            .decodeSingleOrNull<ProfileRow>()
    }

    suspend fun createProfile(username: String): AuthResult<ProfileRow> {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
            ?: return AuthResult.Failure("You're not signed in.", AuthErrorCode.UNKNOWN)

        val trimmed = username.trim()
        if (trimmed.length < 3) {
            return AuthResult.Failure("Username must be at least 3 characters.", AuthErrorCode.UNKNOWN)
        }

        return try {
            val created = supabaseClient.postgrest["profiles"]
                .insert(ProfileRow(id = userId, username = trimmed)) { select() }
                .decodeSingle<ProfileRow>()
            AuthResult.Success(created)
        } catch (e: Exception) {
            val message = if (e.message?.contains("duplicate", ignoreCase = true) == true) {
                "That username is taken — try another."
            } else {
                "Couldn't save your username. Check your connection and try again."
            }
            AuthResult.Failure(message, AuthErrorCode.UNKNOWN)
        }
    }
}