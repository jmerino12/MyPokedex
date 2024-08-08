package com.auth.domain.exceptions

import androidx.annotation.StringRes
import com.auth.domain.exceptions.AuthException

class PasswordException(@StringRes messageResId: Int) : AuthException(messageResId)
