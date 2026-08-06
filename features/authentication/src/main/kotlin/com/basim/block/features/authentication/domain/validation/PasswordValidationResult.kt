package com.basim.block.features.authentication.domain.validation

data class PasswordValidationResult(
    val errors: Set<PasswordValidationError>
) {
    val isValid: Boolean
        get() = errors.isEmpty()
}