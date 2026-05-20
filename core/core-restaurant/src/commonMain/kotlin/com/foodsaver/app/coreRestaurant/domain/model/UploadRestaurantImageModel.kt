package com.foodsaver.app.coreRestaurant.domain.model

class UploadRestaurantImageModel(
    val image: ByteArray,
    val mimeType: String,
    val enterpriseId: String
)