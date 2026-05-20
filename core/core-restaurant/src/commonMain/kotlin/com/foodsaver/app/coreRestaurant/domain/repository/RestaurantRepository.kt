package com.foodsaver.app.coreRestaurant.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel
import com.foodsaver.app.coreRestaurant.domain.model.UpsertRestaurantRequest
import com.foodsaver.app.coreRestaurant.domain.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

interface EditRestaurantRepository {
    suspend fun uploadRestaurantImage(image: ByteArray, restaurantId: String?): ApiResult<String>
    suspend fun upsertRestaurant(request: UpsertRestaurantRequest): ApiResult<RestaurantModel>
}

interface RestaurantRepository {

    fun observeRestaurants(): Flow<ApiResult<List<RestaurantModel>>>
    suspend fun fetchAllRestaurants(): ApiResult<List<RestaurantModel>>

    suspend fun getCachedRestaurants(): ApiResult<List<RestaurantModel>>

    suspend fun getNearestRestaurants(userLocationModel: UserLocationModel): ApiResult<List<RestaurantModel>>
    suspend fun getRestaurantById(restaurantId: String): ApiResult<RestaurantModel>

    suspend fun getAllRestaurants(page: Int, size: Int): ApiResult<List<RestaurantModel>>

    suspend fun getSuggestedRestaurants(): ApiResult<List<RestaurantModel>>
}