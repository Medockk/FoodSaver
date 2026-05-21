@file:OptIn(ExperimentalCoroutinesApi::class)

package com.foodsaver.app.coreProductModule.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.databases.cache.ProductCacheEntity
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.dto.Page
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.requireUserId
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProductModule.data.mappers.mapDtoToEntity
import com.foodsaver.app.coreProductModule.data.mappers.mapEntityToModel
import com.foodsaver.app.coreProductModule.data.mappers.mapRequestToDto
import com.foodsaver.app.coreProductModule.data.mappers.toModel
import com.foodsaver.app.coreProductModule.domain.model.AddProductModel
import com.foodsaver.app.coreProductModule.domain.model.UpdateProductRequest
import com.foodsaver.app.coreProductModule.domain.repository.EditProductRepository
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

internal class ProductRepositoryImpl(
    private val userManager: AuthUserManager,
    private val httpClient: HttpClient,
    databaseProvider: DatabaseProvider,
) : ReadProductRepository, EditProductRepository {

    private val db by lazy { databaseProvider() }

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
                        db.productEntityQueries.upsertProduct(dto.mapDtoToEntity())
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
                db.productEntityQueries.upsertProduct(dto.mapDtoToEntity())
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

    override suspend fun fetchUserProducts(): ApiResult<List<ProductModel>> {
        return withContext(Dispatchers.InputOutput) {
            val user = db.userEntityQueries.getUserById(userManager.requireUserId())
                .executeAsOneOrNull() ?: return@withContext ApiResult.success(emptyList())

                // TODO сделать экран где ROLE_ADMIN видит все продукты и может удалить продукты
            if (user.restaurantId == null) return@withContext ApiResult.success(emptyList())
            saveNetworkCall<Page<ProductDto>> {
                httpClient.get(HttpConstants.PRODUCTS_URL + "/restaurant") {
                    parameter("restaurantId", user.restaurantId)
                }
            }.onSuccess { page ->
                db.transaction {
                    // очищаем старые продукты этого ресторана, чтобы не плодить дубли
                    user.restaurantId?.let { restaurantId ->
                        db.productEntityQueries.deleteProductsByRestaurantId(restaurantId)
                    }

                    // Записываем новые продукты в БД
                    page.content.forEach { product ->
                        db.productEntityQueries.upsertProduct(
                            productCacheEntity = product.mapDtoToEntity()
                        )
                    }
                }
            }.map { dtos -> dtos.content.toModel() }
        }
    }

    override fun observeUserProducts(): Flow<ApiResult<List<ProductModel>>> {
        return db.userEntityQueries.getUserById(userManager.requireUserId())
            .asFlow()
            .mapToOneOrNull(Dispatchers.InputOutput)
            .flatMapLatest { user ->
                if (user == null) {
                    return@flatMapLatest flowOf(emptyList())
                }

                // Проверяем роль
                if (user.authorities.contains("ROLE_ADMIN")) {
                    // Админ видит все продукты из базы данных
                    db.productEntityQueries.getAllProducts()
                        .asFlow()
                        .mapToList(Dispatchers.InputOutput)
                } else {
                    // Менеджер видит только продукты своего ресторана
                    val restaurantId = user.restaurantId ?: ""
                    db.productEntityQueries.getProductByRestaurantId(restaurantId)
                        .asFlow()
                        .mapToList(Dispatchers.InputOutput)
                }
            }
            // Маппим список Entity-моделей из БД в бизнес-модели ProductModel
            .map { entityList ->
                val domainProducts = entityList.map { it.mapEntityToModel() }
                ApiResult.success(domainProducts)
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
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<Unit?> {
                httpClient.delete(HttpConstants.PRODUCTS_URL + "/delete") {
                    parameter("id", productId)
                }
            }.onSuccess { db.productEntityQueries.deleteProductById(productId) }.map {  }
        }
    }

    override suspend fun updateProduct(request: UpdateProductRequest): ApiResult<ProductModel> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<ProductDto> {
                httpClient.patch(HttpConstants.PRODUCTS_URL + "/update") {
                    setBody(request.mapRequestToDto())
                }
            }.onSuccess {
                db.productEntityQueries.upsertProduct(it.mapDtoToEntity())
            }.map { it.toModel() }
        }
    }
}