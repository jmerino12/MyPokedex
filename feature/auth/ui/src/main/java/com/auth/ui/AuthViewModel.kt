package com.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.repository.AuthRepository
import com.core.common.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.UNAUTHENTICATED)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = AuthUiState.LOADING
            authRepository.authState
                .collect { user ->
                    if (user != null) {
                        _uiState.value = AuthUiState.AUTHENTICATED
                    } else {
                        _uiState.value = AuthUiState.UNAUTHENTICATED
                    }
                }

        }
    }

    fun logout() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = AuthUiState.LOADING
            try {
                authRepository.logout()
            } catch (e: Exception) {

            }
        }
    }


}