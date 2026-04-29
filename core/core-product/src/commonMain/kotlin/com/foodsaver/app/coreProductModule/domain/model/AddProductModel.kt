package com.foodsaver.app.coreProductModule.domain.model

import kotlinx.datetime.LocalDate

data class AddProductModel(
    val title: String,
    val description: String,
    val photo: ByteArray,

    val cost: Float,
    val costUnit: String,
    val categoryIds: List<String>,

    val count: Long = 1,
    val unit: Long,
    val unitName: String,
    val ingredients: List<String>,

    val expiresAt: LocalDate
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AddProductModel

        if (cost != other.cost) return false
        if (count != other.count) return false
        if (unit != other.unit) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (!photo.contentEquals(other.photo)) return false
        if (costUnit != other.costUnit) return false
        if (categoryIds != other.categoryIds) return false
        if (unitName != other.unitName) return false
        if (expiresAt != other.expiresAt) return false
        if (ingredients != other.ingredients) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cost.hashCode()
        result = 31 * result + count.hashCode()
        result = 31 * result + unit.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + costUnit.hashCode()
        result = 31 * result + categoryIds.hashCode()
        result = 31 * result + unitName.hashCode()
        result = 31 * result + ingredients.hashCode()
        result = 31 * result + expiresAt.hashCode()
        return result
    }
}
