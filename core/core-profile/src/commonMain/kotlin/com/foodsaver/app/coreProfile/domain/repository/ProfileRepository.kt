package com.foodsaver.app.coreProfile.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.coreProfile.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile(): Flow<ApiResult<UserModel>>
}