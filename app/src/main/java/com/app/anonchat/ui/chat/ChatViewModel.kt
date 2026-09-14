package com.app.anonchat.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.anonchat.data.chat.ChatRepository
import com.app.anonchat.data.chat.MessageRow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<MessageRow> = emptyList(),
    val draft: String = "",
    val isSending: Boolean = false,
    val myUserId: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val conversationId: String = checkNotNull(savedStateHandle["conversationId"])

    private val _uiState = MutableStateFlow(ChatUiState(myUserId = chatRepository.currentUserId()))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                val messages = chatRepository.getMessages(conversationId)
                _uiState.value = _uiState.value.copy(messages = messages)
                delay(2000)
            }
        }
    }

    fun onDraftChange(value: String) {
        _uiState.value = _uiState.value.copy(draft = value)
    }

    fun sendMessage() {
        val text = _uiState.value.draft
        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)
            val result = chatRepository.sendMessage(conversationId, text)
            _uiState.value = _uiState.value.copy(isSending = false)
            if (result is com.app.anonchat.domain.model.AuthResult.Success) {
                _uiState.value = _uiState.value.copy(draft = "")
                val messages = chatRepository.getMessages(conversationId)
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }
}