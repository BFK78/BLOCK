package com.basim.block.features.authentication.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) {
    suspend fun signIn(email: String, password: String): FirebaseUser =
        firebaseAuth.signInWithEmailAndPassword(email, password).await().user
            ?: error("Firebase returned no user after sign-in")

    suspend fun signUp(email: String, password: String): FirebaseUser =
        firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            ?: error("Firebase returned no user after sign-up")
}
