package com.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(authRepository: AuthRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.UNAUTHENTICATED)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
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


}