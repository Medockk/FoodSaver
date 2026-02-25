package com.foodsaver.app.coreProductModule.data.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.map
import com.foodsaver.app.commonModule.ApiResult.onSuccess
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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
        return saveNetworkCall<List<ProductDto>> {
            httpClient.get(HttpConstants.PRODUCTS_URL) {
                parameter("page", page)
                parameter("size", size)
            }
        }.onSuccess { productsDto ->
            val queries = databaseProvider.get().cachedProductQueries

            queries.transaction {
                productsDto.forEach { dto ->
                    queries.insertCachedProduct(dto)
                }
            }
        }.map { it.toModel() }
    }

    override suspend fun getCachedProduct(productId: String): Flow<ProductModel?> = channelFlow {
        val queries = databaseProvider.get().cachedProductQueries

        val product = queries.getCachedProducts().executeAsList()
            .find { it.product.productId == productId }
        send(product?.product?.toModel())
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

    override suspend fun addProduct(addProductModel: AddProductModel): ApiResult<Unit> {

        val dto = json.encodeToString(addProductModel.toDto())

        return saveNetworkCall<ProductDto> {
            httpClient.post(HttpConstants.PRODUCTS_URL) {
                setBody(
                    MultiPartFormDataContent(
                    parts = formData {
                        append("file", addProductModel.photo, Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"photo.png\"")
                        })

                        append("product", dto, Headers.build {
                            append(HttpHeaders.ContentType, "application/json")
                        })
                    }
                )
                )
            }
        }.onSuccess {
            val queries = databaseProvider.get().cachedProductQueries
            queries.insertCachedProduct(it)
        }.map { }
    }

    override suspend fun deleteProduct(productId: String): ApiResult<Unit> {
        TODO("Not yet implemented")
    }
}