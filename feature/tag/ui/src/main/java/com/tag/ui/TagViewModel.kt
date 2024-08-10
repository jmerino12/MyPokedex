package com.tag.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.IoDispatcher
import com.tag.domain.service.TagService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val tagService: TagService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TagUiState>(TagUiState.LOADING)
    val uiState: StateFlow<TagUiState> = _uiState.asStateFlow()

    fun getAllTags() {
        _uiState.value = TagUiState.LOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                tagService.getAllTag().collect {
                    if (it.isEmpty()) {
                        _uiState.value = TagUiState.EMPTY
                    } else {
                        _uiState.value = TagUiState.SUCCESS(it)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = TagUiState.ERROR
            }
        }
    }

}