package com.foodsaver.app.addProductModule.domain.repository

import com.foodsaver.app.addProductModule.domain.model.AddProductRequest
import com.foodsaver.app.addProductModule.domain.model.UploadImageModel
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreModel.model.ProductModel

interface AddProductRepository {

    suspend fun uploadImage(image: ByteArray, productId: String? = null): ApiResult<UploadImageModel>
    suspend fun addProduct(request: AddProductRequest): ApiResult<ProductModel>

    suspend fun fetchCurrencies(): ApiResult<List<String>>
}