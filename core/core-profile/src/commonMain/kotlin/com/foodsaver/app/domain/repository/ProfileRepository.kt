package com.foodsaver.app.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile(): Flow<ApiResult<UserModel>>
}