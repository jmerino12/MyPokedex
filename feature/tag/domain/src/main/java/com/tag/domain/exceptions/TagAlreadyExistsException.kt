package com.tag.domain.exceptions

import androidx.annotation.StringRes

class TagAlreadyExistsException(@StringRes messageResId: Int) : TagException(messageResId)
