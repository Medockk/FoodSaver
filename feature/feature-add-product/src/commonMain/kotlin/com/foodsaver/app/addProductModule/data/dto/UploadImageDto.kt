package com.foodsaver.app.addProductModule.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class UploadImageDto(
    val relativeUri: String,
    val absoluteUri: String
)