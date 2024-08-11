package com.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.exceptions.AuthException
import com.auth.domain.model.LoginWithEmailAndPassword
import com.auth.domain.model.Register
import com.auth.domain.repository.AuthRepository
import com.auth.ui.AuthUiState
import com.auth.ui.R
import com.auth.ui.login.LoginUiState
import com.core.common.IoDispatcher
import com.google.firebase.auth.FirebaseAuthException
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

    private val _errorMessage = MutableStateFlow<Int?>(null)
    val errorMessage: StateFlow<Int?> = _errorMessage.asStateFlow()

    fun register(email: String, password: String, name: String) {
        _uiState.value = RegisterUiState.LOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                authRepository.registerWithEmailAndPassword(Register(name, email, password))
                _uiState.value = RegisterUiState.SUCCESS
            } catch (e: FirebaseAuthException) {
                handleFirebaseError(e)
            } catch (e: AuthException) {
                handleExceptionOfDomain(e)
            }
        }
    }

    fun clearMessage() {
        _errorMessage.value = null
    }

    private fun handleFirebaseError(firebaseAuthException: FirebaseAuthException) {
        when (firebaseAuthException.errorCode) {
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                _errorMessage.value = R.string.invalid_email_already_in_use
            }

            else -> {
                _errorMessage.value = R.string.general_error
            }
        }
        _uiState.value = RegisterUiState.INITIAL
    }

    private fun handleExceptionOfDomain(e: AuthException) {
        _uiState.value = RegisterUiState.INITIAL
        _errorMessage.value = e.messageResId
    }
}