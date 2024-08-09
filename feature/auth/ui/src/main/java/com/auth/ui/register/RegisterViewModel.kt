package com.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.model.LoginWithEmailAndPassword
import com.auth.domain.model.Register
import com.auth.domain.repository.AuthRepository
import com.auth.ui.AuthUiState
import com.core.common.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.INITIAL)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(email: String, password: String, name: String) {
        _uiState.value = RegisterUiState.LOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                authRepository.registerWithEmailAndPassword(Register(name, email, password))
                _uiState.value = RegisterUiState.SUCCESS
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.ERROR
            }
        }
    }
}