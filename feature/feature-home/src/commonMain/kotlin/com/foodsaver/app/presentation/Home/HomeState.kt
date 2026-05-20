package com.foodsaver.app.presentation.Home

import androidx.compose.ui.text.input.TextFieldValue
import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.coreProfile.domain.model.ProfileModel

data class HomeState(
    val deliverTo: String = "",
    val searchQuery: TextFieldValue = TextFieldValue(),

    val categories: List<CategoryModel> = emptyList(),

    val restaurants: List<RestaurantModel> = emptyList(),
    val isRestaurantsLoading: Boolean = false,

    val isRefresh: Boolean = false,

    val selectedCategoryIds: Set<String> = emptySet(),
    val isCategoriesLoading: Boolean = true,

    val cartSize: Long? = null,
    val cartPrice: Double = 0.0,
    val cartId: String? = null,
    val profile: ProfileModel? = null,
)