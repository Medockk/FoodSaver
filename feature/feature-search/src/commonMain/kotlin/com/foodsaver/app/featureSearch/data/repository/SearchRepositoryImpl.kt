@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.featureSearch.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.databases.cache.ProductCacheEntity
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.dto.Page
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.mappers.toModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.featureSearch.domain.model.RecentKeywordsModel
import com.foodsaver.app.featureSearch.domain.repository.SearchRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class SearchRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider,
) : SearchRepository {

    private val db by lazy {
        provider.invoke()
    }

    override suspend fun search(
        query: String,
        page: Int,
        size: Int,
    ): ApiResult<List<ProductModel>> {
        return withContext(Dispatchers.InputOutput) {

            launch {
                db.recentKeywordsEntityQueries
                    .insertValue(
                        id = Uuid.random().toString(),
                        value_ = query,
                        addedAt = Clock.System.now()
                    )
            }

            // todo: make search into local database
            launch {

            }

            return@withContext saveNetworkCall<Page<ProductDto>> {
                httpClient.get(HttpConstants.PRODUCTS_URL + "/search/query") {
                    parameter("q", query)
                    parameter("page", page)
                    parameter("size", size)
                }
            }.onSuccess { page ->
                db.productEntityQueries.transaction {
                    page.content.forEach { product ->
                        db.productEntityQueries.insertProduct(product.mapToEntity())
                    }
                }
            }.map { page ->
                page.content.map { it.toModel() }
            }
        }
    }

    override suspend fun searchByCategoryId(
        categoryId: String,
        page: Int,
        size: Int,
    ): ApiResult<List<ProductModel>> {
        return withContext(Dispatchers.InputOutput) {
            // todo: make search into local database

            return@withContext saveNetworkCall<Page<ProductDto>> {
                httpClient.get(HttpConstants.PRODUCTS_URL + "/search/category") {
                    parameter("categoryId", categoryId)
                    parameter("page", page)
                    parameter("size", size)
                }
            }.onSuccess { page ->
                db.productEntityQueries.transaction {
                    page.content.forEach { dto ->
                        db.productEntityQueries.insertProduct(dto.mapToEntity())
                    }
                }
            }.map { page ->
                page.content.map { it.toModel() }
            }
        }
    }

    override fun getRecentKeywords(): Flow<List<RecentKeywordsModel>> {
        return db.recentKeywordsEntityQueries
            .getValues()
            .asFlow()
            .mapToList(Dispatchers.InputOutput)
            .map { entities ->
                entities.map { entity ->
                    RecentKeywordsModel(
                        id = entity.id,
                        value = entity.value_
                    )
                }
            }
    }

    private fun ProductDto.mapToEntity() = ProductCacheEntity(
        productId = productId,
        name = name,
        description = description,
        imageUris = imageUris,
        price = price,
        discount = discount,
        currency = currency,
        unit = unit,
        restaurantId = restaurantId,
        expiresAt = expiresAt,
        ingredientIds = ingredientIds
    )

    override suspend fun onRecentKeywordSearch(recentKeywordId: String) {
        withContext(Dispatchers.InputOutput) {
            db.recentKeywordsEntityQueries
                .updateDate(
                    addedAt = Clock.System.now(),
                    id = recentKeywordId
                )
        }
    }
}