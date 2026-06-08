package com.foodsaver.app.coreFcm.service

interface FcmService {

    suspend fun getFcmToken(): String?
    suspend fun saveFcmToken(token: String)
}

expect class FcmServiceImpl: FcmService {
    override suspend fun getFcmToken(): String?
    override suspend fun saveFcmToken(token: String)
}