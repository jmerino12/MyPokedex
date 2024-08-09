package com.auth.ui.register

sealed class RegisterUiState {
    data object INITIAL : RegisterUiState()
    data object LOADING : RegisterUiState()
    data object SUCCESS : RegisterUiState()
    data object ERROR : RegisterUiState()
}