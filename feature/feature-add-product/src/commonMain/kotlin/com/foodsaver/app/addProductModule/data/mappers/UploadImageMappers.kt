package com.foodsaver.app.addProductModule.data.mappers

import com.foodsaver.app.addProductModule.data.dto.UploadImageDto
import com.foodsaver.app.addProductModule.domain.model.UploadImageModel

internal fun UploadImageDto.mapDtoToModel() = UploadImageModel(
    relativeUri = relativeUri,
    absoluteUri = absoluteUri
)