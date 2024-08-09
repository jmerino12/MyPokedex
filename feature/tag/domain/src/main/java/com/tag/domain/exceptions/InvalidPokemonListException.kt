package com.tag.domain.exceptions

import androidx.annotation.StringRes

class InvalidPokemonListException(@StringRes messageResId: Int) : TagException(messageResId)
