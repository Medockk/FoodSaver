package com.foodsaver.app.featureProductDetail.presentation.productDetail

import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.featureProductDetail.domain.model.IngredientModel

data class ProductDetailState(
    val product: ProductModel? = null,

    val productCount: Long = 1,
    val totalCost: Float? = null,

    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isProductInCart: Boolean = false,

    val ingredients: IngredientModel? = null,
    val isIngredientMenuExpanded: Boolean = false,
    val isAiResponseLoading: Boolean = false,
    val ingredientsAIDescription: String? = null
)
