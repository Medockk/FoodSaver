package com.foodsaver.app.domain.usecase.personalInfo

import com.foodsaver.app.domain.model.ProfilePersonalInfoModel
import com.foodsaver.app.domain.repository.ProfilePersonalInfoRepository

class SavePersonalInfoUseCase(
    private val repository: ProfilePersonalInfoRepository
) {

    suspend operator fun invoke(request: ProfilePersonalInfoModel) =
        repository.save(request)
}