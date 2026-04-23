package com.foodsaver.app.featureWidget.utils

import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.featureWidget.model.WidgetProductModel
import kotlinx.serialization.json.Json

object WidgetUtils {

    fun toWidgetModel(dto: ProductModel): WidgetProductModel = with(dto) {
        return WidgetProductModel(
            title = this.title,
            imageUri = this.photoUrl,
            price = this.cost,
            productId = this.productId,
            description = this.description,
        )
    }

    fun fromJson(json: String): List<WidgetProductModel> {
        return if (json.isNotBlank()) {
            try {
                Json.decodeFromString(json)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}