package com.foodsaver.app.coreEnterprises.data.repository

import app.cash.sqldelight.coroutines.asFlow
import com.databases.cache.RestaurantEntity
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.dto.Page
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreEnterprises.data.dto.RestaurantDto
import com.foodsaver.app.coreEnterprises.data.mappers.mapToModel
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.coreEnterprises.domain.model.UploadRestaurantImageModel
import com.foodsaver.app.coreEnterprises.domain.model.UserLocationModel
import com.foodsaver.app.coreEnterprises.domain.repository.EditRestaurantRepository
import com.foodsaver.app.coreEnterprises.domain.repository.RestaurantRepository
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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class RestaurantRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider
) : RestaurantRepository, EditRestaurantRepository {

    override suspend fun getCachedRestaurants(): ApiResult<List<RestaurantModel>> {
        return withContext(Dispatchers.InputOutput) {
            val database = databaseProvider.invoke()
            val restaurantQueries = database.restaurantEntityQueries

            val restaurants = restaurantQueries.getAllRestaurants().executeAsList()
                .map {
                    // TODO make normal mapper
                    RestaurantModel(
                        id = it.serverId,
                        name = it.name,
                        description = it.description,
                        photoUris = it.photoUris ?: emptyList(),
                        longitude = it.longitude,
                        latitude = it.latitude,
                        addressName = it.addressName,
                        rating = it.rating,
                        deliveryCost = it.deliveryCost,
                        averageDeliveryTime = it.averageDeliveryTime,
                        companyId = it.companyId
                    )
                }

            return@withContext ApiResult.success(restaurants)
        }
    }

    override suspend fun getNearestRestaurants(userLocationModel: UserLocationModel): ApiResult<List<RestaurantModel>> {
        // TODO
        return saveNetworkCall<List<RestaurantDto>> {
            httpClient.get(HttpConstants.ENTERPRISE_URL) {
                parameter("latitude", userLocationModel.latitude)
                parameter("longitude", userLocationModel.longitude)
            }
        }.map { enterprises ->
            enterprises.map { it.mapToModel() }
        }
    }

    override suspend fun getRestaurantById(restaurantId: String): ApiResult<RestaurantModel> =
        withContext(Dispatchers.InputOutput) {
            val response: ApiResult<RestaurantDto> = saveNetworkCall {
                httpClient.get(HttpConstants.ENTERPRISE_URL + "/id") {
                    parameter("restaurantId", restaurantId)
                }
            }
            return@withContext response.map { it.mapToModel() }
        }

    override suspend fun getAllRestaurants(
        page: Int,
        size: Int
    ): ApiResult<List<RestaurantModel>> = withContext(Dispatchers.InputOutput) {
//        return@withContext channelFlow {
//            send(ApiResult.loading())
//
//            val database = databaseProvider.invoke()
//            val restaurantQueries = database.restaurantEntityQueries
//
//            val databaseJob = launch {
//                restaurantQueries.getAllRestaurants().asFlow().collect { query ->
//                    val restaurants = query.executeAsList()
//                        .map {
//                            // TODO make normal mapper
//                            RestaurantModel(
//                                id = it.serverId,
//                                name = it.name,
//                                description = it.description,
//                                photoUris = it.photoUris ?: emptyList(),
//                                longitude = it.longitude,
//                                latitude = it.latitude,
//                                addressName = it.addressName,
//                                rating = it.rating,
//                                deliveryCost = it.deliveryCost,
//                                averageDeliveryTime = it.averageDeliveryTime,
//                                companyId = it.companyId
//                            )
//                        }
//
//                    send(ApiResult.success(restaurants))
//                }
//            }
//
//            val networkResult = saveNetworkCall<Page<RestaurantDto>> {
//                httpClient.get(HttpConstants.ENTERPRISE_URL + "/all") {
//                    parameter("page", page)
//                    parameter("size", size)
//                }
//            }.onSuccess { page ->
//                launch {
//                    restaurantQueries.transaction {
//                        page.content.forEach {
//                            with(it) {
//                                restaurantQueries.insertRestaurant(
//                                    serverId = id,
//                                    name = name,
//                                    description = description,
//                                    photoUris = photoUris,
//                                    latitude = latitude,
//                                    longitude = longitude,
//                                    addressName = addressName,
//                                    companyId = companyId,
//                                    rating = rating,
//                                    deliveryCost = deliveryCost,
//                                    averageDeliveryTime = averageDeliveryTime
//                                )
//                            }
//                        }
//                    }
//                }
//            }.map { it.content.map { r -> r.mapToModel() } }
//
//            send(networkResult)
//
//            awaitClose {
//                databaseJob.cancel()
//            }
//        }
        return@withContext saveNetworkCall<Page<RestaurantDto>> {
            httpClient.get(HttpConstants.ENTERPRISE_URL + "/all") {
                parameter("page", page)
                parameter("size", size)
            }
        }.onSuccess { page ->
            val restaurantQueries = databaseProvider.invoke().restaurantEntityQueries
            restaurantQueries.transaction {
                page.content.forEach {
                    with(it) {
                        restaurantQueries.insertRestaurant(
                            serverId = id,
                            name = name,
                            description = description,
                            photoUris = photoUris,
                            latitude = latitude,
                            longitude = longitude,
                            addressName = addressName,
                            companyId = companyId,
                            rating = rating,
                            deliveryCost = deliveryCost,
                            averageDeliveryTime = averageDeliveryTime
                        )
                    }
                }
            }

        }.map { it.content.map { r -> r.mapToModel() } }
    }

    override suspend fun getSuggestedRestaurants(): ApiResult<List<RestaurantModel>> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext saveNetworkCall<List<RestaurantDto>> {
                httpClient.get(HttpConstants.ENTERPRISE_URL + "/suggested")
            }.map { restaurants ->
                restaurants.map { it.mapToModel() }
            }
        }
    }

    override suspend fun uploadRestaurantImage(uploadRestaurantImageModel: UploadRestaurantImageModel): ApiResult<String?> =
        withContext(
            Dispatchers.InputOutput
        ) {
            return@withContext saveNetworkCall {
                httpClient.post(HttpConstants.ENTERPRISE_URL + "/uploadImage") {
                    setBody(
                        MultiPartFormDataContent(
                            parts = formData {
                                append("file", uploadRestaurantImageModel.image, Headers.build {
                                    append(
                                        HttpHeaders.ContentType,
                                        uploadRestaurantImageModel.mimeType
                                    )
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"enterprise.jpg\""
                                    )
                                })
                            }
                        ))

                    parameter("enterpriseId", uploadRestaurantImageModel.enterpriseId)
                }
            }
        }
}