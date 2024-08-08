package com.auth.domain.exceptions

import androidx.annotation.StringRes

abstract class AuthException(@StringRes val messageResId: Int) : RuntimeException()
