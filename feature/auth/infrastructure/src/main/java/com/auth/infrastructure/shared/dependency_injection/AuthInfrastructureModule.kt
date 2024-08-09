package com.auth.infrastructure.shared.dependency_injection

import com.auth.domain.repository.AuthRepository
import com.auth.infrastructure.repository.AuthFirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthInfrastructureModule {
    @Provides
    @Singleton
    fun providesFirebaseAuthInstance(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthFirebaseRepository(firebaseAuth)
}