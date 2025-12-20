package com.foodsaver.app.domain.repository

import com.foodsaver.app.ApiResult.ApiResult

interface LogoutRepository {

    suspend fun logout(): ApiResult<Unit>
}