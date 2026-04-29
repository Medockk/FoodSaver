package com.foodsaver.app.coreProductModule.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProductModule.domain.model.AddProductModel
import kotlinx.coroutines.flow.Flow

/**
 * Метод для чтения продуктов из локальной БД и с сервера
 */
interface ReadProductRepository {
    suspend fun getProducts(page: Int, size: Int): ApiResult<List<ProductModel>>
    suspend fun getCachedProduct(productId: String): Flow<ProductModel?>
    suspend fun getCachedProducts(): ApiResult<List<ProductModel>>

    /**
     * Что-то типа пагинации
     * TODO: может вынести в отдельный data class ?
     */
    suspend fun searchProduct(name: String, categoryIds: List<String>, page: Int, size: Int): ApiResult<List<ProductModel>>

}

/**
 * Метод для "особых" пользователей, чтобы добавить/удалить продукт
 */
interface EditProductRepository: ReadProductRepository {
    suspend fun addProduct(addProductModel: AddProductModel): ApiResult<Unit>
    suspend fun deleteProduct(productId: String): ApiResult<Unit>

}