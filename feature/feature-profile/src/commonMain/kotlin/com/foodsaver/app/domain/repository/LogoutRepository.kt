package com.foodsaver.app.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult

interface LogoutRepository {

    suspend fun logout(): ApiResult<Unit>
}