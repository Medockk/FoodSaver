package com.foodsaver.app.domain.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel

interface ProfilePersonalInfoRepository {

    suspend fun save(profilePersonalInfoModel: ProfilePersonalInfoModel): ApiResult<Unit>
}