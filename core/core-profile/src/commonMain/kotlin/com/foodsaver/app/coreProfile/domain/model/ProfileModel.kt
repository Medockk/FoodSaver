@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.coreProfile.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class ProfileModel(
    val id: String,
    val email: String,
    val fullName: String,
    val imageUri: String?,
    val restaurantId: String?,
    val authorities: List<String>,

    val phone: String?,
    val bio: String?
)