package com.foodsaver.app.data.repository

import com.foodsaver.app.data.dto.ProductDto
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.domain.repository.ProductRepository
import com.foodsaver.app.utils.ApiResult.ApiResult
import com.foodsaver.app.utils.ApiResult.map
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductRepositoryImpl(
    private val httpClient: HttpClient,
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
        }.map { it.toModel() }
    }
}