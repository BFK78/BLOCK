package com.basim.block.features.authentication.domain.model

data class User(
    val id: String,
    val email: String?,
    val displayName: String?,
)
