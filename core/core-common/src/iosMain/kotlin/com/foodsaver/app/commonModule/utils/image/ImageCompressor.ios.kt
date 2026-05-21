@file:OptIn(ExperimentalForeignApi::class)

package com.foodsaver.app.commonModule.utils.image

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGAffineTransformMakeRotation
import platform.CoreGraphics.CGContextConcatCTM
import platform.CoreGraphics.CGContextDrawImage
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImageOrientation
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.posix.memcpy

actual object ImageCompressor : ImageCompressorUtils() {
    actual fun compress(
        image: ByteArray,
        quality: Int,
        exifData: ExifData?
    ): ByteArray {
        val nsData = image.toNSData()
        val originalImage = UIImage.imageWithData(nsData) ?: return image

        // Определяем нужную ориентацию для UIImage на основе EXIF
        val iosOrientation = when (exifData?.orientation) {
            ExifOrientation.ORIENTATION_ROTATE_90 -> UIImageOrientation.UIImageOrientationRight
            ExifOrientation.ORIENTATION_ROTATE_180 -> UIImageOrientation.UIImageOrientationDown
            ExifOrientation.ORIENTATION_ROTATE_270 -> UIImageOrientation.UIImageOrientationLeft
            else -> UIImageOrientation.UIImageOrientationUp
        }

        // Создаем новый UIImage с правильной ориентацией
        val rotatedImage = UIImage.imageWithCGImage(
            cgImage = originalImage.CGImage,
            scale = originalImage.scale,
            orientation = iosOrientation
        )

        // Сжимаем в JPEG (качество в iOS передается как Float от 0.0 до 1.0)
        val iosQuality = quality / 100f
        val compressedNSData = UIImageJPEGRepresentation(rotatedImage, iosQuality.toDouble())
            ?: return image

        return compressedNSData.toByteArray()
    }

    actual suspend fun compress(image: ByteArray, exifData: ExifData?): ByteArray {
        return withContext(Dispatchers.Default) {
            var currentQuality = 90
            var resultBytes = image

            // Цикл сжатия по качеству (как и на Android)
            do {
                resultBytes = compress(resultBytes, currentQuality, exifData)
                currentQuality -= 10
            } while (resultBytes.size > 1024 * 1024 * 2 && currentQuality > 20) // Сжимаем до ~2МБ

            resultBytes
        }
    }

    private fun ByteArray.toNSData(): NSData {
        if (isEmpty()) return NSData()
        return this.usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), size.toULong())
        }
    }

    private fun NSData.toByteArray(): ByteArray {
        val byteArray = ByteArray(length.toInt())
        if (byteArray.isEmpty()) return byteArray
        byteArray.usePinned { pinned ->
            memcpy(pinned.addressOf(0), bytes, length)
        }
        return byteArray
    }
}