package com.foodsaver.app.coreProductModule.data.repository

import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.apiResult.saveApiCall
import com.foodsaver.app.commonModule.dto.Page
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProductModule.data.mappers.toDto
import com.foodsaver.app.coreProductModule.data.mappers.toModel
import com.foodsaver.app.coreProductModule.domain.model.AddProductModel
import com.foodsaver.app.coreProductModule.domain.repository.EditProductRepository
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class ProductRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
    private val json: Json,
) : ReadProductRepository, EditProductRepository {

    override suspend fun getProducts(
        page: Int,
        size: Int,
    ): ApiResult<List<ProductModel>> {
        TODO()
    }

    override suspend fun getCachedProduct(productId: String): Flow<ProductModel?> {
        TODO()
    }

    override suspend fun searchProduct(
        name: String,
        categoryIds: List<String>,
        page: Int,
        size: Int,
    ): ApiResult<List<ProductModel>> {
        return saveNetworkCall<List<ProductDto>> {
            httpClient.get(HttpConstants.PRODUCTS_URL + "/search") {
                parameter("name", name)
                categoryIds.forEach { categoryId ->
                    parameter("categoryIds", categoryId)
                }
                parameter("page", page)
                parameter("size", size)
            }
        }.map {
            it.toModel()
        }
    }

    override suspend fun getProductsByRestaurantId(restaurantId: String, page: Int, size: Int): ApiResult<List<ProductModel>> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext saveNetworkCall<Page<ProductDto>> {
                httpClient.get(HttpConstants.PRODUCTS_URL + "/restaurant") {
                    parameter("page", page)
                    parameter("size", size)
                    parameter("restaurantId", restaurantId)
                }
            }.map { page ->
                println("page $page")
                page.content.toModel()
            }
        }
    }

    override suspend fun getProductById(productId: String): ApiResult<ProductModel> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext saveNetworkCall<ProductDto> {
                httpClient.get(HttpConstants.PRODUCTS_URL + "/id") {
                    parameter("productId", productId)
                }
            }.map { it.toModel() }
        }
    }

    override suspend fun addProduct(addProductModel: AddProductModel): ApiResult<Unit> {
        TODO()
    }

    override suspend fun getCachedProducts(): ApiResult<List<ProductModel>> {
        TODO()
    }

    override suspend fun deleteProduct(productId: String): ApiResult<Unit> {
        TODO("хз пока стоит ли тут делать или вынести в editProductRepository")
    }
}