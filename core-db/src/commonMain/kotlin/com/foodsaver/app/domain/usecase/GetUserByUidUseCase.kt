package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.UserDatabaseApi

class GetUserByUidUseCase(
    private val repo: UserDatabaseApi,
) {

    suspend operator fun invoke(uid: String) =
        repo.getUserByUid(uid)
}