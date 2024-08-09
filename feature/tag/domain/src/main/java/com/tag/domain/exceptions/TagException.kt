package com.tag.domain.exceptions

import androidx.annotation.StringRes

abstract class TagException(@StringRes val messageResId: Int) : RuntimeException()