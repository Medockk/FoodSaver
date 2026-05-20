package com.foodsaver.app.coreRestaurant.domain.usecase

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import com.foodsaver.app.coreRestaurant.domain.repository.EditRestaurantRepository

class UploadEnterpriseImageUseCase(
    private val editEnterpriseRepository: EditRestaurantRepository
) {

    suspend operator fun invoke(image: ByteArray, restaurantId: String?): ApiResult<String?> {
        return if (image.isEmpty()) {
            ApiResult.error(GlobalErrorResponse(
                error = "Upload image is Empty!",
                message = "Upload image is Empty!",
                httpCode = 0
            ))
        } else {
            editEnterpriseRepository.uploadRestaurantImage(image, restaurantId)
        }
    }
}