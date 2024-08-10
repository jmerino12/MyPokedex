package com.tag.ui

import com.tag.domain.model.Tag

sealed class TagUiState {
    data object LOADING : TagUiState()
    data class SUCCESS(val tags: List<Tag>) : TagUiState()
    data object EMPTY: TagUiState()
    data object ERROR : TagUiState()
}