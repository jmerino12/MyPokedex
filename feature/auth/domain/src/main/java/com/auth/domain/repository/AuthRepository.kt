package com.auth.domain.repository

import com.auth.domain.model.LoginWithEmailAndPassword
import com.auth.domain.model.Register
import com.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<User?>
    suspend fun logInWithEmailAndPassword(login: LoginWithEmailAndPassword)
    suspend fun registerWithEmailAndPassword(register: Register)
    suspend fun logout()
}