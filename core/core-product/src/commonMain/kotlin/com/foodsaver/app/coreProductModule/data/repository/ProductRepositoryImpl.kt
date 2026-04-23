package com.foodsaver.app.coreProductModule.data.repository

import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.apiResult.saveApiCall
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProductModule.data.mappers.toDto
import com.foodsaver.app.coreProductModule.data.mappers.toModel
import com.foodsaver.app.coreProductModule.domain.model.AddProductModel
import com.foodsaver.app.coreProductModule.domain.repository.EditProductRepository
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class ProductRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
    private val json: Json,
) : ReadProductRepository, EditProductRepository {

    override suspend fun getProducts(
        page: Int,
        size: Int,
    ): ApiResult<List<ProductModel>> {
        return saveNetworkCall<List<ProductDto>> {
            // поиск
            httpClient.get(HttpConstants.PRODUCTS_URL) {
                parameter("page", page)
                parameter("size", size)
                parameter("searchType", "NEARBY") // тип поиска (ближайшее/рекомендованное)
            }
        }.onSuccess { productsDto: List<ProductDto>? ->
            val queries = databaseProvider.get().cachedProductQueries

            queries.transaction {
                // транзакция для того, чтобы не делать N+1 запросы к БД
                productsDto?.forEach { dto ->
                    queries.insertCachedProduct(
                        productId = dto.productId,
                        product = dto
                    )
                }
            }
        }.map { it.toModel() }
    }

    override suspend fun getCachedProduct(productId: String): Flow<ProductModel?> = channelFlow {
        val queries = databaseProvider.get().cachedProductQueries

        val product = try {
            queries.getCachedProductByProductId(productId)
                .executeAsList() // для безопасности, тк метод executeAsOne может выбросить IllegalStateException ->
                // сработает catch блок ->
                // вернёт null
                .firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        send(product?.product?.toModel())
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

    override suspend fun addProduct(addProductModel: AddProductModel): ApiResult<Unit> {

        val dto = json.encodeToString(addProductModel.toDto())

        return saveNetworkCall<ProductDto> {
            httpClient.post(HttpConstants.PRODUCTS_URL) {
                setBody(
                    MultiPartFormDataContent(
                    parts = formData {
                        // через formData тк file - это отдельный request параметр
                        // можно установить только 1 setBody
                        append("file", addProductModel.photo, Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"photo.png\"")
                        })

                        append("product", dto, Headers.build {
                            append(HttpHeaders.ContentType, "application/json")
                        })
                    }
                )
                )
            }
        }.onSuccess {
            // при успехе кэшируем
            val queries = databaseProvider.get().cachedProductQueries
            queries.insertCachedProduct(it.productId, it)
        }.map { }
    }

    override suspend fun getCachedProducts(): ApiResult<List<ProductModel>> =
        withContext(Dispatchers.InputOutput) {
            return@withContext saveApiCall {
                val queries = databaseProvider.get().cachedProductQueries
                val products = queries.getCachedProducts().executeAsList()
                    .map { cachedProduct ->
                        cachedProduct.product.toModel()
                    }
                products
            }
        }

    override suspend fun deleteProduct(productId: String): ApiResult<Unit> {
        TODO("хз пока стоит ли тут делать или вынести в editProductRepository")
    }
}