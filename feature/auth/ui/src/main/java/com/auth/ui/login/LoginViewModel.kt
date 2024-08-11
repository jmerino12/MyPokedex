package com.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.exceptions.AuthException
import com.auth.domain.model.LoginWithEmailAndPassword
import com.auth.domain.repository.AuthRepository
import com.auth.ui.R
import com.core.common.IoDispatcher
import com.google.firebase.auth.FirebaseAuthException

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

    private val _errorMessage = MutableStateFlow<Int?>(null)
    val errorMessage: StateFlow<Int?> = _errorMessage.asStateFlow()

    fun login(email: String, password: String) {
        _uiState.value = LoginUiState.LOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                authRepository.logInWithEmailAndPassword(LoginWithEmailAndPassword(email, password))
                _uiState.value = LoginUiState.SUCCESS
            } catch (e: FirebaseAuthException) {
                handleFirebaseError(e)
            } catch (e: AuthException) {
                handleExceptionOfDomain(e)
            }

        }
    }

    private fun handleExceptionOfDomain(e: AuthException) {
        _uiState.value = LoginUiState.INITIAL
        _errorMessage.value = e.messageResId
    }

    private fun handleFirebaseError(firebaseAuthException: FirebaseAuthException) {
        when (firebaseAuthException.errorCode) {
            "ERROR_INVALID_CREDENTIAL" -> {
                _errorMessage.value = R.string.invalid_credential
            }

            else -> {
                _errorMessage.value = R.string.general_error
            }
        }
        _uiState.value = LoginUiState.INITIAL
    }
}