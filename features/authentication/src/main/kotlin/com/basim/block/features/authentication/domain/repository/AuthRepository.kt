package com.basim.block.features.authentication.domain.repository

import com.basim.block.core.common.result.DataResult
import com.basim.block.features.authentication.domain.model.User

interface AuthRepository {

    /** Sign an existing user in with email + password. */
    suspend fun signIn(email: String, password: String): DataResult<User>

    /** Create a new account with email + password and sign it in. */
    suspend fun signUp(email: String, password: String): DataResult<User>
}
