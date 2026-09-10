package com.app.anonchat.domain.model

import java.time.LocalDate

/**
 * Sealed result wrapper so ViewModels never have to guess whether an
 * operation succeeded — no silent nulls, no thrown exceptions crossing
 * layer boundaries.
 */
sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Failure(val message: String, val code: AuthErrorCode) : AuthResult<Nothing>()
}

enum class AuthErrorCode {
    INVALID_CREDENTIALS,
    UNDER_AGE,
    RATE_LIMITED,
    ACCOUNT_LOCKED,
    NETWORK_ERROR,
    UNKNOWN
}

data class SignUpRequest(
    val email: String,
    val password: String,
    val dateOfBirth: LocalDate
) {
    /** Client-side pre-check only — the server independently re-validates age. */
    fun meetsMinimumAge(minimumAge: Int = 18): Boolean {
        val today = LocalDate.now()
        var age = today.year - dateOfBirth.year
        if (today.dayOfYear < dateOfBirth.dayOfYear) age--
        return age >= minimumAge
    }
}

data class LoginRequest(
    val email: String,
    val password: String
)
