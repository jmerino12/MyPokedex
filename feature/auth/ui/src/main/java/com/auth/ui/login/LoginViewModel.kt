package com.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.model.LoginWithEmailAndPassword
import com.auth.domain.repository.AuthRepository
import com.core.common.IoDispatcher

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.INITIAL)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        _uiState.value = LoginUiState.LOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                authRepository.logInWithEmailAndPassword(LoginWithEmailAndPassword(email, password))
                _uiState.value = LoginUiState.SUCCESS
            } catch (e: Exception) {
                _uiState.value = LoginUiState.ERROR
            }
        }
    }
}