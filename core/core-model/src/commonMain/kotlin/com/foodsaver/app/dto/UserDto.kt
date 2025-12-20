@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.dto

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class UserDto(
    val uid: String,
    val username: String,
    val email: String?,
    val name: String?,
    val photoUrl: String?,
    val createdAt: Instant,
    val roles: List<String>,

    val phone: String?,
    val bio: String?,
    val addresses: List<String> = emptyList(),
    val paymentCartNumbers: List<String> = emptyList()
)
