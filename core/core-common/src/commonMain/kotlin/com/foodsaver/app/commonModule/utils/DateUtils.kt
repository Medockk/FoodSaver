package com.foodsaver.app.commonModule.utils

import kotlinx.datetime.LocalDate

class DateUtils {

    /**
     * Parse [value] to [LocalDate] in format dd-MM-yyyy or null, if [value] isn't date
     */
    fun parseToLocalDate(value: String): LocalDate? {
        val format = LocalDate.Format {
            day(); monthNumber(); year()
        }

        return try {
            LocalDate.parse(value, format)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}