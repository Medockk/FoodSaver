package com.foodsaver.app.domain.repository

import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.utils.ApiResult.ApiResult

interface ProductRepository {

    suspend fun getProducts(page: Int, size: Int): ApiResult<List<ProductModel>>
}