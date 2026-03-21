package com.foodsaver.app.domain.usecase.personalInfo

import com.foodsaver.app.domain.repository.ProfilePersonalInfoRepository

class UploadAvatarUseCase(
    private val repository: ProfilePersonalInfoRepository
) {

    suspend operator fun invoke(
        bytes: ByteArray,
        contentType: String = "image/png",
        fileName: String = "avatar",
    ) = repository.uploadAvatar(
        bytes = bytes,
        contentType = contentType,
        fileName = fileName
    )
}