package com.auth.domain.model

import com.auth.domain.R
import com.auth.domain.exceptions.EmailException
import com.auth.domain.exceptions.PasswordException
import java.util.regex.Pattern

data class LoginWithEmailAndPassword(
    val email: String,
    val password: String
) {
    companion object {
        const val REGEX_EMAIL_VALIDATION = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
    }

    init {
        if (!validateEmail()) {
            throw EmailException(R.string.email_exception_message)
        }

        if (!validatePassword()) {
            throw PasswordException(R.string.password_exception_message)
        }
    }

    private fun validateEmail(): Boolean {
        return Pattern.matches(REGEX_EMAIL_VALIDATION, this.email)
    }

    private fun validatePassword(): Boolean {
        return this.password.isNotEmpty()
    }
}
