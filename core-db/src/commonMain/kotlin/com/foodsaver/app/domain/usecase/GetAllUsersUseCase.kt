package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.UserDatabaseApi

class GetAllUsersUseCase(
    private val userDatabaseApi: UserDatabaseApi
) {

    suspend operator fun invoke() =
        userDatabaseApi.getAllUsers()
}