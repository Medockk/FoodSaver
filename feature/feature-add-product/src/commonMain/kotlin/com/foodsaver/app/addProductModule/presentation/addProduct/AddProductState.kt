package com.foodsaver.app.addProductModule.presentation.addProduct

//import kotlinx.datetime.LocalDateTime
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.format
//import kotlinx.datetime.format.char
//import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class AddProductState(
    val title: String = "",
    val description: String = "",
    val cost: String = "",
    val costUnit: String = "",
    val selectedCategories: List<String> = emptyList(),

    val count: String = "1",
    val unit: String = "",
    val unitName: String = "",

//    val expiresAt: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
//        .format(LocalDateTime.Format {
//            day(); char('-') ;monthNumber(); char('-'); year()
//        })
)
