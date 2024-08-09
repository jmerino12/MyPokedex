package com.auth.infrastructure.anticorruption

import com.auth.domain.model.User
import com.google.firebase.auth.FirebaseUser

class UserTranslate {
    companion object {
        fun fromDtoToDomain(firebaseUser: FirebaseUser): User {
            return User(
                firebaseUser.uid,
                firebaseUser.email.toString(),
                firebaseUser.displayName.toString(),
            )
        }
    }
}