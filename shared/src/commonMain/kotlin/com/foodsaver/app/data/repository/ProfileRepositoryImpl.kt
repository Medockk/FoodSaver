package com.foodsaver.app.data.repository

import com.foodsaver.app.data.data_source.remote.HttpConstants
import com.foodsaver.app.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ProfileRepositoryImpl(
    private val client: HttpClient,
) : ProfileRepository {

    override suspend fun getAllUsers(): Result<List<String>> {
        try {
            val result: List<String> = client.get("${HttpConstants.BASE_URL}users/all").body()

            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
