package com.foodsaver.app.commonModule.utils.image

actual object ImageCompressor : ImageCompressorUtils() {
    actual fun compress(image: ByteArray, quality: Int, exifData: ExifData? = null): ByteArray {
        TODO("Not yet implemented")
    }
}