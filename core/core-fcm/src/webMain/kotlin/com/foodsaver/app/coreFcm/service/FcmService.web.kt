package com.foodsaver.app.coreFcm.service

actual class FcmServiceImpl : FcmService {

    actual override suspend fun getFcmToken(): String? {
        println("FCM Service Not implemented")
        return null
    }

    actual override suspend fun saveFcmToken(token: String) {
        println("FCM Service Not implemented")
    }
}