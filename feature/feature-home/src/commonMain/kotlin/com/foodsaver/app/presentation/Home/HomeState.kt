package com.foodsaver.app.presentation.Home

import androidx.compose.ui.text.input.TextFieldValue
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.coreModel.model.AddressModel
import com.foodsaver.app.coreModel.model.CategoryModel
import com.foodsaver.app.coreModel.model.OrganizationModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProfile.domain.model.UserModel
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.OfferModel

data class HomeState(
    val deliverTo: String = "",
    val searchQuery: TextFieldValue = TextFieldValue(),

    val categories: List<CategoryModel> = emptyList(),

    val restaurants: List<RestaurantModel> = emptyList(),
    val isRestaurantsLoading: Boolean = false,

    val isRefresh: Boolean = false,

    val selectedCategoryIds: Set<String> = emptySet(),
    val isCategoriesLoading: Boolean = true,

    val cartSize: Int = 0,
    val profile: UserModel? = null,
)