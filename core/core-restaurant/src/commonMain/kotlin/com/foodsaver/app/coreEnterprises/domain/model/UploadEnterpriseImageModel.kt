package com.foodsaver.app.coreEnterprises.domain.model

class UploadEnterpriseImageModel(
    val image: ByteArray,
    val mimeType: String,
    val enterpriseId: String
)