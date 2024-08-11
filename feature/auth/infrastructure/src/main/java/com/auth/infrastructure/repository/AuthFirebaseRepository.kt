package com.auth.infrastructure.repository

import com.auth.domain.model.LoginWithEmailAndPassword
import com.auth.domain.model.Register
import com.auth.domain.model.User
import com.auth.domain.repository.AuthRepository
import com.auth.infrastructure.anticorruption.UserTranslate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthFirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val authState: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser?.let { UserTranslate.fromDtoToDomain(it) }
            trySend(user)
        }
        auth.addAuthStateListener(listener)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override suspend fun logInWithEmailAndPassword(login: LoginWithEmailAndPassword) {
        singIn(login)
    }

    override suspend fun registerWithEmailAndPassword(register: Register) {
        try {
            auth.createUserWithEmailAndPassword(register.email, register.password).await()
            singIn(LoginWithEmailAndPassword(register.email, register.password))
        } catch (e: FirebaseAuthException) {
            throw e
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }


    private suspend fun singIn(login: LoginWithEmailAndPassword) {
        try {
            auth.signInWithEmailAndPassword(login.email, login.password).await()
        } catch (e: FirebaseAuthException) {
            throw e
        }
    }

}