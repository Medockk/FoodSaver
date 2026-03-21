package com.foodsaver.app.commonModule.utils.image

enum class ExifOrientation {
    ORIENTATION_ROTATE_90,
    ORIENTATION_ROTATE_180,
    ORIENTATION_ROTATE_270,
    ORIENTATION_NORMAL,
}

object ExifOrientationParser {
    fun parseStringOrientation(value: String?): ExifOrientation {
        return when (value) {
            "Rotate 180° [3]" -> ExifOrientation.ORIENTATION_ROTATE_180
            "Rotate 90° CW [6]" -> ExifOrientation.ORIENTATION_ROTATE_90
            "Rotate 90° CCW [8]" -> ExifOrientation.ORIENTATION_ROTATE_270
            else -> ExifOrientation.ORIENTATION_NORMAL
        }
    }
}

data class ExifData(
    val orientation: ExifOrientation
)
