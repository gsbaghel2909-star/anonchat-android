package com.app.anonchat.data.chat

import com.app.anonchat.domain.model.AuthErrorCode
import com.app.anonchat.domain.model.AuthResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class ConversationRow(
    val id: String,
    val user_a: String,
    val user_b: String
)

@Serializable
data class MessageRow(
    val id: String,
    val conversation_id: String,
    val sender_id: String,
    val body: String,
    val created_at: String
)

@Singleton
class ChatRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    fun currentUserId(): String? = supabaseClient.auth.currentUserOrNull()?.id

    suspend fun getOrCreateConversationId(otherUserId: String): AuthResult<String> {
        val myId = currentUserId()
            ?: return AuthResult.Failure("You're not signed in.", AuthErrorCode.UNKNOWN)

        val userA = if (myId < otherUserId) myId else otherUserId
        val userB = if (myId < otherUserId) otherUserId else myId

        return try {
            val existing = supabaseClient.postgrest["conversations"]
                .select {
                    filter {
                        eq("user_a", userA)
                        eq("user_b", userB)
                    }
                }
                .decodeSingleOrNull<ConversationRow>()

            if (existing != null) {
                AuthResult.Success(existing.id)
            } else {
                val created = supabaseClient.postgrest["conversations"]
                    .insert(mapOf("user_a" to userA, "user_b" to userB)) { select() }
                    .decodeSingle<ConversationRow>()
                AuthResult.Success(created.id)
            }
        } catch (e: Exception) {
            AuthResult.Failure("Couldn't open this chat. Try again.", AuthErrorCode.UNKNOWN)
        }
    }

    suspend fun getMessages(conversationId: String): List<MessageRow> {
        return try {
            supabaseClient.postgrest["messages"]
                .select {
                    filter { eq("conversation_id", conversationId) }
                    order(column = "created_at", order = Order.ASCENDING)
                }
                .decodeList<MessageRow>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun sendMessage(conversationId: String, body: String): AuthResult<Unit> {
        val myId = currentUserId()
            ?: return AuthResult.Failure("You're not signed in.", AuthErrorCode.UNKNOWN)
        if (body.isBlank()) return AuthResult.Failure("Message can't be empty.", AuthErrorCode.UNKNOWN)

        return try {
            supabaseClient.postgrest["messages"].insert(
                mapOf(
                    "conversation_id" to conversationId,
                    "sender_id" to myId,
                    "body" to body.trim()
                )
            )
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure("Couldn't send. Check your connection.", AuthErrorCode.UNKNOWN)
        }
    }
}