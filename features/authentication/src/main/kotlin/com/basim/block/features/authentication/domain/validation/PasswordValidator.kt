package com.basim.block.features.authentication.domain.validation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordValidator @Inject constructor() {

    operator fun invoke(password: String): PasswordValidationResult {
        val errors = buildSet {
            if (password.length < MIN_PASSWORD_LENGTH) add(PasswordValidationError.TOO_SHORT)
            if (!password.any(Char::isLowerCase)) add(PasswordValidationError.MISSING_LOWER_CASE)
            if (!password.any(Char::isUpperCase)) add(PasswordValidationError.MISSING_UPPERCASE)
            if (!password.any(Char::isDigit)) add(PasswordValidationError.MISSING_DIGIT)
        }
        return PasswordValidationResult(errors)
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}