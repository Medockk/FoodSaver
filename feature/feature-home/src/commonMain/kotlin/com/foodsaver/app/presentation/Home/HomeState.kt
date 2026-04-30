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
    val categories: List<CategoryModel> = listOf(
        CategoryModel("Burgers", "2"),
        CategoryModel("Pizza", "3"),
    ),
    val restaurants: List<RestaurantModel> = listOf(
        RestaurantModel(
            id = "",
            latitude = 2.2,
            longitude = 2.3,
            addressName = "Some address name to bla bla bla",
            description = "This is super description 1",
            organization = OrganizationModel(
                id = "",
                organizationName = "OKEI"
            ),
            name = "wdwddw"
        ),
        RestaurantModel(
            id = "",
            latitude = 2.2,
            longitude = 2.3,
            addressName = "Some address name to bla bla bla",
            description = "This is super description 1",
            organization = OrganizationModel(
                id = "",
                organizationName = "OKEI"
            ),
            name = "wdwddw"
        ),
        RestaurantModel(
            id = "",
            latitude = 2.2,
            longitude = 2.3,
            addressName = "Some address name to bla bla bla",
            description = "This is super description 1",
            organization = OrganizationModel(
                id = "",
                organizationName = "pyatiorochka"
            ),
            name = "wdwddw"
        ),
        RestaurantModel(
            id = "",
            latitude = 2.2,
            longitude = 2.3,
            addressName = "Some address name to bla bla bla",
            description = "This is super description 1",
            organization = OrganizationModel(
                id = "",
                organizationName = "magnit"
            ),
            name = "wdwddw"
        ),
    ),


    val profile: UserModel? = null,
    val currentAddress: AddressModel? = null,

    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val selectedCategoryIds: Set<String> = emptySet(),
    val isCategoriesLoading: Boolean = true,

    val cartProducts: List<CartItemModel> = emptyList(),
    val cartProductIds: Set<String> = emptySet(),

    val offers: List<OfferModel> = emptyList(),
    val isOffersLoading: Boolean = true,

    val products: List<ProductModel> = emptyList(),
    val isProductsLoading: Boolean = products.isEmpty(),

    val searchedProducts: List<ProductModel> = emptyList(),
    val productsDisplayMode: ProductsDisplayMode = ProductsDisplayMode.All,
)

sealed interface ProductsDisplayMode {
    data object All : ProductsDisplayMode
    data object Searched : ProductsDisplayMode
}