package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.map
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.domain.repository.ProductRepository
import com.foodsaver.app.dto.ProductDto
import com.foodsaver.app.model.ProductModel
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

internal class ProductRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider
) : ProductRepository {

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
        size: Int
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
}