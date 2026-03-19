package com.foodsaver.app.featureEnterprises.domain.model

class UploadEnterpriseImageModel(
    val image: ByteArray,
    val mimeType: String,
    val enterpriseId: String
)