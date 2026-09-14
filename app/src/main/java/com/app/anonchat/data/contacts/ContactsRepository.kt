package com.app.anonchat.data.contacts

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
data class ProfileLite(
    val id: String,
    val username: String
)

@Serializable
data class ContactRow(
    val id: String,
    val requester_id: String,
    val addressee_id: String,
    val status: String
)

data class ContactWithProfile(
    val contactId: String,
    val otherUserId: String,
    val otherUsername: String,
    val status: String,
    val iAmRequester: Boolean
)

@Singleton
class ContactsRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    private fun currentUserId(): String? = supabaseClient.auth.currentUserOrNull()?.id

    suspend fun findProfileByUsername(username: String): ProfileLite? {
        return supabaseClient.postgrest["profiles"]
            .select(columns = Columns.list("id", "username")) {
                filter { eq("username", username.trim()) }
            }
            .decodeSingleOrNull<ProfileLite>()
    }

    suspend fun sendContactRequest(addresseeId: String): AuthResult<Unit> {
        val myId = currentUserId()
            ?: return AuthResult.Failure("You're not signed in.", AuthErrorCode.UNKNOWN)

        if (myId == addresseeId) {
            return AuthResult.Failure("You can't add yourself.", AuthErrorCode.UNKNOWN)
        }

        return try {
            supabaseClient.postgrest["contacts"].insert(
                mapOf(
                    "requester_id" to myId,
                    "addressee_id" to addresseeId,
                    "status" to "pending"
                )
            )
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            val message = if (e.message?.contains("duplicate", ignoreCase = true) == true) {
                "You've already sent a request to this person."
            } else {
                "Couldn't send the request. Try again."
            }
            AuthResult.Failure(message, AuthErrorCode.UNKNOWN)
        }
    }

    suspend fun respondToRequest(contactId: String, accept: Boolean): AuthResult<Unit> {
        return try {
            supabaseClient.postgrest["contacts"]
                .update(mapOf("status" to if (accept) "accepted" else "declined")) {
                    filter { eq("id", contactId) }
                }
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure("Couldn't update that request.", AuthErrorCode.UNKNOWN)
        }
    }

    suspend fun removeContact(contactId: String): AuthResult<Unit> {
        return try {
            supabaseClient.postgrest["contacts"].delete {
                filter { eq("id", contactId) }
            }
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure("Couldn't remove this contact.", AuthErrorCode.UNKNOWN)
        }
    }

    suspend fun getMyContacts(): List<ContactWithProfile> {
        val myId = currentUserId() ?: return emptyList()

        val asRequester = supabaseClient.postgrest["contacts"]
            .select { filter { eq("requester_id", myId) } }
            .decodeList<ContactRow>()
        val asAddressee = supabaseClient.postgrest["contacts"]
            .select { filter { eq("addressee_id", myId) } }
            .decodeList<ContactRow>()

        val all = asRequester + asAddressee
        if (all.isEmpty()) return emptyList()

        val otherIds = all.map { if (it.requester_id == myId) it.addressee_id else it.requester_id }.distinct()
        val profiles = supabaseClient.postgrest["profiles"]
            .select(columns = Columns.list("id", "username")) {
                filter { isIn("id", otherIds) }
            }
            .decodeList<ProfileLite>()
            .associateBy { it.id }

        return all.map { row ->
            val iAmRequester = row.requester_id == myId
            val otherId = if (iAmRequester) row.addressee_id else row.requester_id
            ContactWithProfile(
                contactId = row.id,
                otherUserId = otherId,
                otherUsername = profiles[otherId]?.username ?: "Unknown",
                status = row.status,
                iAmRequester = iAmRequester
            )
        }
    }
}