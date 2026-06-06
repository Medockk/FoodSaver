package com.foodsaver.app.coreFcm.dto

import kotlinx.serialization.Serializable

data class FirebaseUpdateTokenRequest(val token: String)

@Serializable
data class FirebaseUpdateTokenDto(val token: String)

internal fun FirebaseUpdateTokenRequest.mapRequestToDto() =
    FirebaseUpdateTokenDto(token)