package com.foodsaver.app.coreProfile.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreProfile.domain.model.ProfileModel
import com.foodsaver.app.coreProfile.domain.model.UpdateProfileRequest
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun observeProfile(): Flow<ApiResult<ProfileModel>>
    suspend fun fetchProfile(): ApiResult<ProfileModel>
    suspend fun updateProfile(request: UpdateProfileRequest, avatar: ByteArray?)
}