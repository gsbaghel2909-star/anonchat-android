package com.app.anonchat.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.anonchat.data.contacts.ContactWithProfile
import com.app.anonchat.data.contacts.ContactsRepository
import com.app.anonchat.domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContactsUiState(
    val isLoading: Boolean = true,
    val contacts: List<ContactWithProfile> = emptyList(),
    val searchUsername: String = "",
    val searchMessage: String? = null,
    val isSearching: Boolean = false
)

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val contacts = contactsRepository.getMyContacts()
            _uiState.value = _uiState.value.copy(isLoading = false, contacts = contacts)
        }
    }

    fun onSearchUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(searchUsername = value, searchMessage = null)
    }

    fun sendRequest() {
        val username = _uiState.value.searchUsername
        if (username.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true, searchMessage = null)

            val profile = contactsRepository.findProfileByUsername(username)
            if (profile == null) {
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    searchMessage = "No user found with that username."
                )
                return@launch
            }

            when (val result = contactsRepository.sendContactRequest(profile.id)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        searchUsername = "",
                        searchMessage = "Request sent!"
                    )
                    refresh()
                }
                is AuthResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        searchMessage = result.message
                    )
                }
            }
        }
    }

    fun respondToRequest(contactId: String, accept: Boolean) {
        viewModelScope.launch {
            contactsRepository.respondToRequest(contactId, accept)
            refresh()
        }
    }

    fun removeContact(contactId: String) {
        viewModelScope.launch {
            contactsRepository.removeContact(contactId)
            refresh()
        }
    }
}