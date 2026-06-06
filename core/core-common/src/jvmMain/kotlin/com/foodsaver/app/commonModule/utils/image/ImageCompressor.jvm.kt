package com.foodsaver.app.commonModule.utils.image

actual object ImageCompressor : ImageCompressorUtils() {
    actual fun compress(
        image: ByteArray,
        quality: Int,
        exifData: ExifData?,
    ): ByteArray {
        TODO("Not yet implemented")
    }

    actual suspend fun compress(image: ByteArray, exifData: ExifData?): ByteArray {
        TODO("Not yet implemented")
    }
}