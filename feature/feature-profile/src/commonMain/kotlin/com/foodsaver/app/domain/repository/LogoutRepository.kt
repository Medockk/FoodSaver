package com.foodsaver.app.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult

interface LogoutRepository {

    suspend fun logout(): ApiResult<Unit>
}