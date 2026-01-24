@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.coreProfile.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class UserModel(
    val uid: String,
    val username: String,
    val email: String?,
    val name: String?,
    val photoUrl: String?,
    val createdAt: Instant,
    val roles: List<String>,

    val phone: String?,
    val bio: String?,
)