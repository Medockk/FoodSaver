package com.foodsaver.app.data.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.ApiResult.map
import com.foodsaver.app.data.dto.CategoryDto
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.CategoryModel
import com.foodsaver.app.domain.repository.CategoryRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

internal class CategoryRepositoryImpl(
    private val httpClient: HttpClient
): CategoryRepository {

    override suspend fun getAllCategories(): ApiResult<List<CategoryModel>> {
        return saveNetworkCall<List<CategoryDto>> {
            httpClient.get("${HttpConstants.BASE_URL}products/allCategories")
        }.map { it.toModel() }
    }
}