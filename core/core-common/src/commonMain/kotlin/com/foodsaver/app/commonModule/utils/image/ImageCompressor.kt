package com.foodsaver.app.commonModule.utils.image

abstract class ImageCompressorUtils {

    fun isImageSizeValid(image: ByteArray): Boolean {
        val maxSizeInBytes = 1024 * 1024 * 10 // 10 MB

        return image.size < maxSizeInBytes
    }
}

expect object ImageCompressor: ImageCompressorUtils {

    fun compress(image: ByteArray, quality: Int, exifData: ExifData? = null): ByteArray
}