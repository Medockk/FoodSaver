package com.foodsaver.app.coreCategory.data.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.coreCategory.domain.repository.CategoryRepository
import com.foodsaver.app.coreModel.dto.CategoryDto
import com.foodsaver.app.coreModel.mappers.mapToCategoryModel
import com.foodsaver.app.coreModel.model.CategoryModel
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

internal class CategoryRepositoryImpl(
    private val httpClient: HttpClient
): CategoryRepository {

    override suspend fun getAllCategories(): ApiResult<List<CategoryModel>> {
        return saveNetworkCall<List<CategoryDto>> {
            httpClient.get(HttpConstants.CATEGORY_URL)
        }.map { it.mapToCategoryModel() }
    }
}