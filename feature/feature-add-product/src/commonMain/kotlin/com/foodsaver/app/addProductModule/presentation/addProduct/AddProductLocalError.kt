package com.foodsaver.app.addProductModule.presentation.addProduct

import com.foodsaver.app.commonModule.utils.uiText.LocalError
import foodsaver.feature.feature_add_product.generated.resources.Res
import foodsaver.feature.feature_add_product.generated.resources.empty_category
import foodsaver.feature.feature_add_product.generated.resources.empty_currency
import foodsaver.feature.feature_add_product.generated.resources.empty_images
import foodsaver.feature.feature_add_product.generated.resources.empty_ingredient
import foodsaver.feature.feature_add_product.generated.resources.empty_name
import foodsaver.feature.feature_add_product.generated.resources.empty_price
import foodsaver.feature.feature_add_product.generated.resources.low_price
import foodsaver.feature.feature_add_product.generated.resources.empty_expires_date
import foodsaver.feature.feature_add_product.generated.resources.expires_date_mismatch
import foodsaver.feature.feature_add_product.generated.resources.empty_unit
import org.jetbrains.compose.resources.StringResource

sealed interface AddProductLocalError: LocalError<StringResource> {

    data object EmptyName: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.empty_name
    }

    data object EmptyImages: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.empty_images
    }

    data object EmptyPrice: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.empty_price
    }

    data object LowPrice: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.low_price
    }

    data object EmptyCurrency: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.empty_currency
    }

    data object EmptyCategory: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.empty_category
    }

    data object EmptyIngredients: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.empty_ingredient
    }

    data object EmptyExpiresDate: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.empty_expires_date
    }

    data object ExpiresDateMismatch: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.expires_date_mismatch
    }

    data object EmptyUnit: AddProductLocalError {
        override val error: StringResource
            get() = Res.string.empty_unit
    }
}