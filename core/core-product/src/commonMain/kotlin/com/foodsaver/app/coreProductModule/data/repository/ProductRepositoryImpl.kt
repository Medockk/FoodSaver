package com.foodsaver.app.coreProductModule.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.dto.Page
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProductModule.data.mappers.mapDtoToEntity
import com.foodsaver.app.coreProductModule.data.mappers.mapEntityToModel
import com.foodsaver.app.coreProductModule.data.mappers.toModel
import com.foodsaver.app.coreProductModule.domain.model.AddProductModel
import com.foodsaver.app.coreProductModule.domain.repository.EditProductRepository
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class ProductRepositoryImpl(
    private val httpClient: HttpClient,
    databaseProvider: DatabaseProvider,
) : ReadProductRepository, EditProductRepository {

    private val db = databaseProvider()

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

    override suspend fun fetchProductByRestaurantId(
        restaurantId: String,
        page: Int,
        size: Int,
    ): ApiResult<List<ProductModel>> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext saveNetworkCall<Page<ProductDto>> {
                httpClient.get(HttpConstants.PRODUCTS_URL + "/restaurant") {
                    parameter("page", page)
                    parameter("size", size)
                    parameter("restaurantId", restaurantId)
                }
            }.onSuccess { page ->
                db.productEntityQueries.transaction {
                    page.content.forEach { dto ->
                        db.productEntityQueries.insertProduct(dto.mapDtoToEntity())
                    }
                }
            }.map { page ->
                page.content.toModel()
            }
        }
    }

    override suspend fun fetchProductById(productId: String): ApiResult<ProductModel> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext saveNetworkCall<ProductDto> {
                httpClient.get(HttpConstants.PRODUCTS_URL + "/id") {
                    parameter("productId", productId)
                }
            }.onSuccess { dto ->
                db.productEntityQueries.insertProduct(dto.mapDtoToEntity())
            }.map { it.toModel() }
        }
    }

    override suspend fun getSuggestedProducts(): ApiResult<List<ProductModel>> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext saveNetworkCall<List<ProductDto>> {
                httpClient.get(HttpConstants.PRODUCTS_URL + "/suggested")
            }.map { dtos -> dtos.toModel() }
        }
    }

    override fun observeProductsByRestaurantId(restaurantId: String): Flow<ApiResult<List<ProductModel>>> {
        return db.productEntityQueries.getProductByRestaurantId(restaurantId)
            .asFlow()
            .mapToList(Dispatchers.InputOutput)
            .map { entities ->
                val products = entities.map { it.mapEntityToModel() }
                ApiResult.success(products)
            }
    }

    override fun observeProductById(productId: String): Flow<ApiResult<ProductModel>> {
        return db.productEntityQueries.getProduct(productId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.InputOutput)
            .map {
                if (it != null) ApiResult.success(it.mapEntityToModel())
                else ApiResult.loading()
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