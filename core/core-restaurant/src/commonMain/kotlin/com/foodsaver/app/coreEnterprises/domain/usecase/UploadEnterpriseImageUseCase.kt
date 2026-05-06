package com.foodsaver.app.coreEnterprises.domain.usecase

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import com.foodsaver.app.coreEnterprises.domain.model.UploadRestaurantImageModel
import com.foodsaver.app.coreEnterprises.domain.repository.EditRestaurantRepository

class UploadEnterpriseImageUseCase(
    private val editEnterpriseRepository: EditRestaurantRepository
) {

    suspend operator fun invoke(uploadRestaurantImageModel: UploadRestaurantImageModel): ApiResult<String?> {
        return if (uploadRestaurantImageModel.image.isEmpty()) {
            ApiResult.error(GlobalErrorResponse(
                error = "Upload image is Empty!",
                message = "Upload image is Empty!",
                httpCode = 0
            ))
        } else {
            editEnterpriseRepository.uploadRestaurantImage(uploadRestaurantImageModel)
        }
    }
}