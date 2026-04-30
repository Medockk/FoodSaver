package com.foodsaver.app.coreEnterprises.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreEnterprises.domain.model.EnterpriseImagesModel
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.coreEnterprises.domain.model.UploadEnterpriseImageModel
import com.foodsaver.app.coreEnterprises.domain.model.UserLocationModel

interface EditEnterpriseRepository : EnterprisesRepository {
    suspend fun uploadEnterpriseImage(uploadEnterpriseImageModel: UploadEnterpriseImageModel): ApiResult<String?>
}

interface EnterprisesRepository {

    suspend fun getNearestEnterprises(userLocationModel: UserLocationModel): ApiResult<List<RestaurantModel>>
    suspend fun getEnterpriseById(enterpriseId: String): ApiResult<RestaurantModel?>
    suspend fun getEnterpriseImageUrls(enterpriseId: String): ApiResult<List<EnterpriseImagesModel>>
}