package com.basim.block.features.authentication.data.repository

import com.basim.block.core.common.result.DataResult
import com.basim.block.core.common.result.safeCall
import com.basim.block.features.authentication.data.mapper.toAuthAppError
import com.basim.block.features.authentication.data.mapper.toDomain
import com.basim.block.features.authentication.data.remote.FirebaseAuthDataSource
import com.basim.block.features.authentication.domain.model.User
import com.basim.block.features.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: FirebaseAuthDataSource,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): DataResult<User> =
        safeCall(mapError = Throwable::toAuthAppError) {
            dataSource.signIn(email, password).toDomain()
        }

    override suspend fun signUp(email: String, password: String): DataResult<User> =
        safeCall(mapError = Throwable::toAuthAppError) {
            dataSource.signUp(email, password).toDomain()
        }
}
