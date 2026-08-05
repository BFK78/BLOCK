package com.basim.block.features.authentication.data.mapper

import com.basim.block.features.authentication.domain.model.User
import com.google.firebase.auth.FirebaseUser

/** Firebase's user type → the domain [User]. Keeps Firebase types out of the domain. */
fun FirebaseUser.toDomain(): User = User(
    id = uid,
    email = email,
    displayName = displayName,
)
