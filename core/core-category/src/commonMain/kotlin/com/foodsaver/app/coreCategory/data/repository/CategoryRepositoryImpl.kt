package com.foodsaver.app.coreCategory.data.repository

import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.dto.Page
import com.foodsaver.app.coreCategory.data.dto.CategoryDto
import com.foodsaver.app.coreCategory.data.mappers.mapDtoToModel
import com.foodsaver.app.coreCategory.data.mappers.mapRequestToDto
import com.foodsaver.app.coreCategory.domain.model.AddCategoryRequest
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.coreCategory.domain.model.UpdateCategoryRequest
import com.foodsaver.app.coreCategory.domain.repository.CategoryRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class CategoryRepositoryImpl(
    private val httpClient: HttpClient
): CategoryRepository {

    override suspend fun fetchAllCategories(): ApiResult<List<CategoryModel>> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<Page<CategoryDto>> {
                httpClient.get(HttpConstants.CATEGORY_URL + "/all")
            }.map { page ->
                page.content.map { it.mapDtoToModel() }
            }
        }
    }

    override suspend fun fetchCategoryById(id: String): ApiResult<CategoryModel> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<CategoryDto> {
                httpClient.get(HttpConstants.CATEGORY_URL + "/id") {
                    parameter("id", id)
                }
            }.map { it.mapDtoToModel() }
        }
    }

    override suspend fun addCategory(request: AddCategoryRequest): ApiResult<CategoryModel> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<CategoryDto> {
                httpClient.post(HttpConstants.CATEGORY_URL + "/add") {
                    setBody(request.mapRequestToDto())
                }
            }.map { it.mapDtoToModel() }
        }
    }

    override suspend fun updateCategory(request: UpdateCategoryRequest): ApiResult<CategoryModel> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<CategoryDto> {
                httpClient.put(HttpConstants.CATEGORY_URL + "/update") {
                    setBody(request.mapRequestToDto())
                }
            }.map { it.mapDtoToModel() }
        }
    }
}