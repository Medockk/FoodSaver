package com.foodsaver.app.coreFcm.service

actual class FcmServiceImpl : FcmService {

    actual override suspend fun getFcmToken(onComplete: (String?) -> Unit) {
        TODO("Not yet implemented")
    }

    actual override suspend fun saveFcmToken(token: String) {
        TODO("Not yet implemented")
    }
}
