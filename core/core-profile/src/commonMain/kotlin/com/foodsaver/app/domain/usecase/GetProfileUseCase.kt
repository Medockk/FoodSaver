package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) {

    operator fun invoke() =
        repository.getProfile()
}