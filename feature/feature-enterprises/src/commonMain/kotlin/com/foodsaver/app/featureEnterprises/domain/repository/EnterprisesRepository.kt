package com.foodsaver.app.featureEnterprises.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.featureEnterprises.domain.model.EnterpriseImagesModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterprisesModel
import com.foodsaver.app.featureEnterprises.domain.model.UploadEnterpriseImageModel
import com.foodsaver.app.featureEnterprises.domain.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

interface EditEnterpriseRepository: EnterprisesRepository {
    suspend fun uploadEnterpriseImage(uploadEnterpriseImageModel: UploadEnterpriseImageModel): ApiResult<String?>
}

interface EnterprisesRepository {

    suspend fun getNearestEnterprises(userLocationModel: UserLocationModel): ApiResult<List<EnterprisesModel>>
    suspend fun getEnterpriseById(enterpriseId: String): ApiResult<EnterprisesModel?>
    suspend fun getEnterpriseImageUrls(enterpriseId: String): ApiResult<List<EnterpriseImagesModel>>
}