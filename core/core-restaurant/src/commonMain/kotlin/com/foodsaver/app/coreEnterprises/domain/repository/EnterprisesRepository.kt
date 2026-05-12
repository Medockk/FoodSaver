package com.foodsaver.app.coreEnterprises.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.coreEnterprises.domain.model.UploadRestaurantImageModel
import com.foodsaver.app.coreEnterprises.domain.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

interface EditRestaurantRepository : RestaurantRepository {
    suspend fun uploadRestaurantImage(uploadRestaurantImageModel: UploadRestaurantImageModel): ApiResult<String?>
}

interface RestaurantRepository {

    suspend fun getCachedRestaurants(): ApiResult<List<RestaurantModel>>

    suspend fun getNearestRestaurants(userLocationModel: UserLocationModel): ApiResult<List<RestaurantModel>>
    suspend fun getRestaurantById(restaurantId: String): ApiResult<RestaurantModel>

    suspend fun getAllRestaurants(page: Int, size: Int): ApiResult<List<RestaurantModel>>

    suspend fun getSuggestedRestaurants(): ApiResult<List<RestaurantModel>>
}