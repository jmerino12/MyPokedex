package com.tag.domain.exceptions

import androidx.annotation.StringRes

class TagNotFoundException(@StringRes messageResId: Int) : TagException(messageResId)
