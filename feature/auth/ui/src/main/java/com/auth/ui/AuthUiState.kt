package com.auth.ui

sealed class AuthUiState {
    data object LOADING : AuthUiState()
    data object AUTHENTICATED : AuthUiState()
    data object UNAUTHENTICATED : AuthUiState()
}