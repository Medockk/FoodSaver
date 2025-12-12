package com.foodsaver.app.data.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.ApiResult.map
import com.foodsaver.app.ApiResult.onSuccess
import com.foodsaver.app.data.dto.ProductDto
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.domain.repository.ProductRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

private typealias ProductId = String

class ProductRepositoryImpl(
    private val httpClient: HttpClient,
) : ProductRepository {

    private val _productsCache = MutableStateFlow<Map<ProductId, ProductDto>>(emptyMap())

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
            _productsCache.value = productsDto.associateBy { it.productId }
        }.map { it.toModel() }
    }

    override fun getCachedProduct(productId: String): Flow<ProductModel?> {
        val product = _productsCache.map { it.getOrElse(productId) { null } }
        return product.map { it?.toModel() }
    }
}