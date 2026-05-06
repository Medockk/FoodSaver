package com.foodsaver.app.coreFcm.service

actual class FcmServiceImpl : FcmService {

    actual override suspend fun getFcmToken(onComplete: (String?) -> Unit) {
        onComplete(null)
    }

    actual override suspend fun saveFcmToken(token: String) {

    }
}
