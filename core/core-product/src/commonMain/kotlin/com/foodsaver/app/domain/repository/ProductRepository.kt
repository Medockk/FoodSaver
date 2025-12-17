package com.foodsaver.app.domain.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProducts(page: Int, size: Int): ApiResult<List<ProductModel>>
    fun getCachedProduct(productId: String): Flow<ProductModel?>
}