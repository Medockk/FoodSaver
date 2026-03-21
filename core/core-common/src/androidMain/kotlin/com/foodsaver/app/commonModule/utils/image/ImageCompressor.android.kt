package com.foodsaver.app.commonModule.utils.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.ByteArrayOutputStream

actual object ImageCompressor : ImageCompressorUtils() {
    actual fun compress(image: ByteArray, quality: Int, exifData: ExifData?): ByteArray {
        val originalBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)

        println("EXIF $exifData")
        val rotatedImage = when (exifData?.orientation) {
            ExifOrientation.ORIENTATION_ROTATE_90 -> rotateImage(originalBitmap, 90f)
            ExifOrientation.ORIENTATION_ROTATE_180 -> rotateImage(originalBitmap, 180f)
            ExifOrientation.ORIENTATION_ROTATE_270 -> rotateImage(originalBitmap, 270f)
            else -> originalBitmap
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        rotatedImage.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)

        if (originalBitmap != rotatedImage) originalBitmap.recycle()

        return byteArrayOutputStream.toByteArray()
    }

    private fun rotateImage(image: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
    }
}