package com.foodsaver.app.commonModule.utils.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

// android
actual object ImageCompressor : ImageCompressorUtils() {
    actual fun compress(image: ByteArray, quality: Int, exifData: ExifData?): ByteArray {
        val originalBitmap = BitmapFactory.decodeByteArray(image, 0, image.size) ?: return image

        val matrix = Matrix().apply {
            when (exifData?.orientation) {
                ExifOrientation.ORIENTATION_ROTATE_90 -> postRotate(90f)
                ExifOrientation.ORIENTATION_ROTATE_180 -> postRotate(180f)
                ExifOrientation.ORIENTATION_ROTATE_270 -> postRotate(270f)
                else -> {}
            }
        }

        // Поворачиваем только если это необходимо
        val rotatedBitmap = if (!matrix.isIdentity) {
            Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true).also {
                originalBitmap.recycle() // Сразу освобождаем оригинал
            }
        } else {
            originalBitmap
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        rotatedBitmap.recycle() // Освобождаем память

        return byteArrayOutputStream.toByteArray()
    }

    actual suspend fun compress(image: ByteArray, exifData: ExifData?): ByteArray {
        // Запускаем на Dispatchers.Default
        return withContext(Dispatchers.Default) {
            val originalBitmap =
                BitmapFactory.decodeByteArray(image, 0, image.size) ?: return@withContext image

            // Если картинка гигантская - стоит уменьшить её разрешение до адекватных 2000px по широкой стороне.
            val matrix = Matrix().apply {
                when (exifData?.orientation) {
                    ExifOrientation.ORIENTATION_ROTATE_90 -> postRotate(90f)
                    ExifOrientation.ORIENTATION_ROTATE_180 -> postRotate(180f)
                    ExifOrientation.ORIENTATION_ROTATE_270 -> postRotate(270f)
                    else -> {}
                }
            }

            val rotatedBitmap = if (!matrix.isIdentity) {
                Bitmap.createBitmap(
                    originalBitmap,
                    0,
                    0,
                    originalBitmap.width,
                    originalBitmap.height,
                    matrix,
                    true
                ).also {
                    originalBitmap.recycle()
                }
            } else {
                originalBitmap
            }

            var currentQuality = 90
            var resultBytes: ByteArray

            // Цикл сжатия без пересоздания Bitmap
            do {
                val os = ByteArrayOutputStream()
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, os)
                resultBytes = os.toByteArray()
                currentQuality -= 10
            } while (resultBytes.size > 1024 * 1024 * 2 && currentQuality > 20) // Сжимаем до 2МБ

            rotatedBitmap.recycle()
            resultBytes
        }
    }
}