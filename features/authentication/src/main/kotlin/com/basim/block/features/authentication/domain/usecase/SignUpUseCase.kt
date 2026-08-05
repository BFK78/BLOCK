package com.basim.block.features.authentication.domain.usecase

import com.basim.block.core.common.result.DataResult
import com.basim.block.features.authentication.domain.model.User
import com.basim.block.features.authentication.domain.repository.AuthRepository
import javax.inject.Inject

/** Registers a new user. Trims the email so trailing whitespace never breaks the account. */
class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): DataResult<User> =
        repository.signUp(email.trim(), password)
}
