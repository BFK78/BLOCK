package com.basim.block.features.authentication.domain.usecase

import com.basim.block.core.common.result.DataResult
import com.basim.block.features.authentication.domain.model.User
import com.basim.block.features.authentication.domain.repository.AuthRepository
import javax.inject.Inject

// TODO: Not removing the Usecase because in the future we would need to add complex validation and other logic here.
class SignInUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): DataResult<User> =
        repository.signIn(email.trim(), password)
}
