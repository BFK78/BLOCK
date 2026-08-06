package com.basim.block.features.authentication.domain.validation

enum class PasswordValidationError {
    TOO_SHORT,
    MISSING_DIGIT,
    MISSING_LOWER_CASE,
    MISSING_UPPERCASE,
}