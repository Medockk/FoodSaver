package com.foodsaver.app.coreCategory.data.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.dto.Page
import com.foodsaver.app.coreCategory.data.dto.CategoryDto
import com.foodsaver.app.coreCategory.data.mappers.mapDtoToModel
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.coreCategory.domain.repository.CategoryRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

internal class CategoryRepositoryImpl(
    private val httpClient: HttpClient
): CategoryRepository {

    override suspend fun fetchAllCategories(): ApiResult<List<CategoryModel>> {
        return saveNetworkCall<Page<CategoryDto>> {
            httpClient.get(HttpConstants.CATEGORY_URL + "/all")
        }.map { page ->
            page.content.map { it.mapDtoToModel() }
        }
    }
}