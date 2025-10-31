package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.repository.UserDatabaseApi

class InsertUserUseCase(
    private val repo: UserDatabaseApi,
) {

    suspend operator fun invoke(userModel: UserModel): Result<UserModel> {
        return try {
            Result.success(repo.insertUser(userModel))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}