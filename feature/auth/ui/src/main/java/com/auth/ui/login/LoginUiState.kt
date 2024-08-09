package com.auth.ui.login

sealed class LoginUiState {
    data object INITIAL : LoginUiState()
    data object LOADING : LoginUiState()
    data object SUCCESS : LoginUiState()
    data object ERROR : LoginUiState()
}