package com.foodsaver.app.coreEnterprises.domain.model

class UploadRestaurantImageModel(
    val image: ByteArray,
    val mimeType: String,
    val enterpriseId: String
)