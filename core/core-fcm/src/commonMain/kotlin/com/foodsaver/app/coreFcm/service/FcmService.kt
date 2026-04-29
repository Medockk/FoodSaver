package com.foodsaver.app.coreFcm.service

interface FcmService {

    suspend fun getFcmToken(onComplete: (String?) -> Unit)
    suspend fun saveFcmToken(token: String)
}

expect class FcmServiceImpl: FcmService {
    override suspend fun getFcmToken(onComplete: (String?) -> Unit)
    override suspend fun saveFcmToken(token: String)
}