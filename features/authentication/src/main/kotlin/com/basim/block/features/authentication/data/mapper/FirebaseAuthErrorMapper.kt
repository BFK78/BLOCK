package com.basim.block.features.authentication.data.mapper

import com.basim.block.core.common.error.AppError
import com.basim.block.core.common.error.toAppError
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

fun Throwable.toAuthAppError(): AppError = when (this) {
    is FirebaseAuthWeakPasswordException -> AppError.Validation.Invalid(reason = "weak_password")
    is FirebaseAuthInvalidCredentialsException -> AppError.Network.Unauthorized
    is FirebaseAuthInvalidUserException -> AppError.Network.Unauthorized
    is FirebaseAuthUserCollisionException -> AppError.Network.Conflict
    is FirebaseNetworkException -> AppError.Network.Unavailable
    else -> toAppError()
}
