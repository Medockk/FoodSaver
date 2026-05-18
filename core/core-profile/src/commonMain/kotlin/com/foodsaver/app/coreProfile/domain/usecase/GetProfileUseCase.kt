package com.foodsaver.app.coreProfile.domain.usecase

import com.foodsaver.app.coreProfile.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) {

    operator fun invoke() =
        repository.observeProfile()
}