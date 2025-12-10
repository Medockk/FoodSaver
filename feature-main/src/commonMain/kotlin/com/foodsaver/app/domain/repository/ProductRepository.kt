package com.foodsaver.app.domain.repository

import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.utils.ApiResult.ApiResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProducts(page: Int, size: Int): ApiResult<List<ProductModel>>
    fun getCachedProduct(productId: String): Flow<ProductModel?>
}