package com.foodsaver.app.coreRestaurant.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.dto.Page
import com.foodsaver.app.commonModule.utils.image.ExifData
import com.foodsaver.app.commonModule.utils.image.ExifOrientationParser
import com.foodsaver.app.commonModule.utils.image.ImageCompressor
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.requireUserId
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreRestaurant.data.dto.RestaurantDto
import com.foodsaver.app.coreRestaurant.data.mappers.mapEntityToDto
import com.foodsaver.app.coreRestaurant.data.mappers.mapRequestToDto
import com.foodsaver.app.coreRestaurant.data.mappers.mapToModel
import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel
import com.foodsaver.app.coreRestaurant.domain.model.AddRestaurantRequest
import com.foodsaver.app.coreRestaurant.domain.model.UpdateRestaurantRequest
import com.foodsaver.app.coreRestaurant.domain.model.UserLocationModel
import com.foodsaver.app.coreRestaurant.domain.repository.EditRestaurantRepository
import com.foodsaver.app.coreRestaurant.domain.repository.RestaurantRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class RestaurantRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
    private val userManager: AuthUserManager
) : RestaurantRepository, EditRestaurantRepository {

    private val db by lazy { databaseProvider.invoke() }

    override fun observeRestaurants(): Flow<ApiResult<List<RestaurantModel>>> {
        return db.restaurantEntityQueries.getAllRestaurants()
            .asFlow()
            .mapToList(Dispatchers.InputOutput)
            .map { entities ->
                val models = entities.map { it.mapEntityToDto() }
                ApiResult.success(models)
            }
    }

    override suspend fun fetchAllRestaurants(): ApiResult<List<RestaurantModel>> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<Page<RestaurantDto>> {
                httpClient.get(HttpConstants.RESTAURANT_URL + "/all") {
                    parameter("page", 0)
                    parameter("size", 100)
                }
            }.onSuccess { page ->
                db.restaurantEntityQueries.transaction {
                    page.content.forEach { dto ->
                        with(dto) {
                            db.restaurantEntityQueries.insertRestaurant(
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
            }.map { p -> p.content.map { it.mapToModel() } }
        }
    }

    override suspend fun fetchUserRestaurant(): ApiResult<List<RestaurantModel>> {
        return withContext(Dispatchers.InputOutput) {
            val userId = userManager.requireUserId()
            val user = db.userEntityQueries.getUserById(userId)
                .executeAsOneOrNull() ?: return@withContext ApiResult.success(emptyList())
            println("User authorities is ${user.authorities}")

            if (user.authorities.contains("ROLE_ADMIN")) {
                println("USer role is Admin")
                fetchAllRestaurants().onSuccess { println("Restaurant for Admin from network is $it") }
            } else if (user.authorities.contains("ROLE_MANAGER")){
                println("USer role is Manager")
                val userRestaurantId = user.restaurantId ?: return@withContext ApiResult.success(emptyList())
                saveNetworkCall<RestaurantDto> {
                    httpClient.get(HttpConstants.RESTAURANT_URL + "/id") {
                        parameter("restaurantId", userRestaurantId)
                    }
                }.onSuccess { println("Restaurant for Manager from network is $it") }.map { listOf(it.mapToModel()) }
            } else {
                println("USer role isn't Manager and Admin.")
                ApiResult.success(emptyList())
            }

        }
    }

    override fun observeUserRestaurant(): Flow<ApiResult<List<RestaurantModel>>> {
        return channelFlow {
            val userId = userManager.requireUserId()
            val user = db.userEntityQueries.getUserById(userId)
                .executeAsOneOrNull() ?: return@channelFlow
            println("User authorities is ${user.authorities}")

            if (user.authorities.contains("ROLE_ADMIN")) {
                println("USer role is Admin")
                db.restaurantEntityQueries.getAllRestaurants()
                    .asFlow()
                    .mapToList(Dispatchers.InputOutput)
                    .collect { restaurants ->
                        val models = restaurants.map { it.mapEntityToDto() }
                        println("Restaurants for Admin from local database is $restaurants")
                        send(ApiResult.success(models))
                    }
            } else if (user.authorities.contains("ROLE_MANAGER")) {
                println("USer role is Manager")
                user.restaurantId?.let { restaurantId ->
                    db.restaurantEntityQueries.getRestaurantById(restaurantId)
                        .asFlow()
                        .mapToOneOrNull(Dispatchers.InputOutput)
                        .collect { restaurant ->
                            restaurant?.let { restaurant ->
                                println("Restaurant for manager from local database is $restaurant")
                                send(ApiResult.success(listOf(restaurant.mapEntityToDto())))
                            }
                        }
                }
            }
        }
    }

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
            httpClient.get(HttpConstants.RESTAURANT_URL) {
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
                httpClient.get(HttpConstants.RESTAURANT_URL + "/id") {
                    parameter("restaurantId", restaurantId)
                }
            }
            return@withContext response.map { it.mapToModel() }
        }

    override suspend fun getAllRestaurants(
        page: Int,
        size: Int
    ): ApiResult<List<RestaurantModel>> = withContext(Dispatchers.InputOutput) {
        return@withContext saveNetworkCall<Page<RestaurantDto>> {
            httpClient.get(HttpConstants.RESTAURANT_URL + "/all") {
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
                httpClient.get(HttpConstants.RESTAURANT_URL + "/suggested")
            }.map { restaurants ->
                restaurants.map { it.mapToModel() }
            }
        }
    }

    override suspend fun uploadRestaurantImage(
        image: ByteArray,
        restaurantId: String?,
        imageOrientation: String?
    ): ApiResult<String> =
        withContext(Dispatchers.InputOutput) {
            val exif = ExifData(ExifOrientationParser.parseStringOrientation(imageOrientation))
            val image = ImageCompressor.compress(image, exif)
            return@withContext saveNetworkCall {
                httpClient.post(HttpConstants.RESTAURANT_URL + "/uploadImage") {
                    setBody(
                        MultiPartFormDataContent(
                            parts = formData {
                                append("image", image, Headers.build {
                                    append(HttpHeaders.ContentType, "image/png")
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"restaurantImage.png\""
                                    )
                                })
                            }
                        ))

                    restaurantId?.let {
                        parameter("restaurantId", restaurantId)
                    }
                }
            }
        }

    override suspend fun addRestaurant(request: AddRestaurantRequest): ApiResult<RestaurantModel> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<RestaurantDto> {
                httpClient.post(HttpConstants.RESTAURANT_URL + "/add") {
                    setBody(request.mapRequestToDto())
                }
            }.upsertRestaurant().map { it.mapToModel() }
        }
    }

    override suspend fun updateRestaurant(request: UpdateRestaurantRequest): ApiResult<RestaurantModel> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<RestaurantDto> {
                httpClient.put(HttpConstants.RESTAURANT_URL + "/update") {
                    setBody(request.mapRequestToDto())
                }
            }.upsertRestaurant().map { it.mapToModel() }
        }
    }

    private suspend fun ApiResult<RestaurantDto>.upsertRestaurant(): ApiResult<RestaurantDto> {
        return this.onSuccess { dto ->
            with(dto) {
                db.restaurantEntityQueries.insertRestaurant(
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
}