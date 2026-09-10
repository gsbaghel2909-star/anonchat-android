package com.app.anonchat.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.anonchat.data.auth.AuthRepository
import com.app.anonchat.domain.model.AuthResult
import com.app.anonchat.domain.model.LoginRequest
import com.app.anonchat.domain.model.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signUp(email: String, password: String, dateOfBirth: LocalDate) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = authRepository.signUp(SignUpRequest(email, password, dateOfBirth))) {
                is AuthResult.Success -> _uiState.value = AuthUiState.Success
                is AuthResult.Failure -> _uiState.value = AuthUiState.Error(result.message)
            }
        }
    }

    fun login(email: String, password: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = authRepository.login(LoginRequest(email, password))) {
                is AuthResult.Success -> _uiState.value = AuthUiState.Success
                is AuthResult.Failure -> _uiState.value = AuthUiState.Error(result.message)
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
