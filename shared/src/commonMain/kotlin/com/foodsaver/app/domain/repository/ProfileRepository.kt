package com.foodsaver.app.domain.repository

interface ProfileRepository {

    suspend fun getAllUsers(): Result<List<String>>
}