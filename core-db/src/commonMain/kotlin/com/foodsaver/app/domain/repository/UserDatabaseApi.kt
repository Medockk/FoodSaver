package com.foodsaver.app.domain.repository

import com.foodsaver.app.domain.model.UserModel

interface UserDatabaseApi {

    suspend fun getAllUsers(): List<UserModel>
    suspend fun insertUser(userModel: UserModel): UserModel
    suspend fun getUserByUid(uid: String): UserModel?
}