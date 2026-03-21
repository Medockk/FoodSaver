package com.foodsaver.app.featureEnterprises.domain.usecase

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import com.foodsaver.app.featureEnterprises.domain.model.UploadEnterpriseImageModel
import com.foodsaver.app.featureEnterprises.domain.repository.EditEnterpriseRepository

class UploadEnterpriseImageUseCase(
    private val editEnterpriseRepository: EditEnterpriseRepository
) {

    suspend operator fun invoke(uploadEnterpriseImageModel: UploadEnterpriseImageModel): ApiResult<String?> {
        return if (uploadEnterpriseImageModel.image.isEmpty()) {
            ApiResult.Error(GlobalErrorResponse(
                error = "Upload image is Empty!",
                message = "Upload image is Empty!",
                httpCode = 0
            ))
        } else {
            editEnterpriseRepository.uploadEnterpriseImage(uploadEnterpriseImageModel)
        }
    }
}