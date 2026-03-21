package com.foodsaver.app.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel

interface ProfilePersonalInfoRepository {

    suspend fun save(profilePersonalInfoModel: ProfilePersonalInfoModel): ApiResult<Unit>
    suspend fun uploadAvatar(
        bytes: ByteArray,
        contentType: String = "image/png",
        fileName: String = "avatar",
    ): ApiResult<String>
}