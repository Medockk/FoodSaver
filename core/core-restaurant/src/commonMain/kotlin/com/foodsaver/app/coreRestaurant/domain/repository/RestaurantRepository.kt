package com.foodsaver.app.coreRestaurant.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel
import com.foodsaver.app.coreRestaurant.domain.model.AddRestaurantRequest
import com.foodsaver.app.coreRestaurant.domain.model.UpdateRestaurantRequest
import com.foodsaver.app.coreRestaurant.domain.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

interface EditRestaurantRepository {
    suspend fun uploadRestaurantImage(image: ByteArray, restaurantId: String?, imageOrientation: String? = null): ApiResult<String>
    suspend fun addRestaurant(request: AddRestaurantRequest): ApiResult<RestaurantModel>
    suspend fun updateRestaurant(request: UpdateRestaurantRequest): ApiResult<RestaurantModel>

    suspend fun deleteRestaurant(restaurantId: String): ApiResult<Unit>
}

interface RestaurantRepository {

    fun observeRestaurants(): Flow<ApiResult<List<RestaurantModel>>>
    suspend fun fetchAllRestaurants(): ApiResult<List<RestaurantModel>>

    suspend fun fetchUserRestaurant(): ApiResult<List<RestaurantModel>>
    fun observeUserRestaurant(): Flow<ApiResult<List<RestaurantModel>>>

    suspend fun getRestaurantsByIds(ids: List<String>): ApiResult<List<RestaurantModel>>

    suspend fun getCachedRestaurants(): ApiResult<List<RestaurantModel>>

    suspend fun getNearestRestaurants(userLocationModel: UserLocationModel): ApiResult<List<RestaurantModel>>
    suspend fun getRestaurantById(restaurantId: String): ApiResult<RestaurantModel>

    suspend fun getAllRestaurants(page: Int, size: Int): ApiResult<List<RestaurantModel>>

    suspend fun getSuggestedRestaurants(): ApiResult<List<RestaurantModel>>
}