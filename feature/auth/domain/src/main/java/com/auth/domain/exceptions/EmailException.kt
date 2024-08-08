package com.auth.domain.exceptions

import androidx.annotation.StringRes
import com.auth.domain.exceptions.AuthException

class EmailException(@StringRes messageResId: Int) : AuthException(messageResId)
