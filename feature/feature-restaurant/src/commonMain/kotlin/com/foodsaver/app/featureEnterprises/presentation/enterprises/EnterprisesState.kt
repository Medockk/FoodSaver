package com.foodsaver.app.featureEnterprises.presentation.enterprises

import com.foodsaver.app.coreEnterprises.domain.model.CameraPositionModel
import com.foodsaver.app.coreEnterprises.domain.model.EnterpriseImagesModel
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.coreModel.model.ExpiresDateType
import com.foodsaver.app.coreModel.model.OrganizationModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreModel.model.ProductUnitType
import com.foodsaver.app.featureEnterprises.domain.model.RestaurantCategoryModel

data class EnterprisesState(
    val enterprises: List<RestaurantModel> = emptyList(),
    val cameraPositionModel: CameraPositionModel? = null,

    val selectedEnterprise: RestaurantModel? = null,
    val selectedEnterpriseImagesModel: List<EnterpriseImagesModel> = emptyList(),

    val isPickerLauncherOpen: Boolean = false,


    val isFavoriteRestaurant: Boolean = false,
    val restaurant: RestaurantModel? = RestaurantModel(
        id = "",
        latitude = 2.2,
        longitude = 3.4,
        addressName = "Address name something like that",
        description = "Maecenas sed diam eget risus varius blandit sit amet non magna. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.Maecenas sed diam eget risus varius blandit sit amet non magna. Integer posuere erat a ante venenatis dapibus posuere velit aliquet.",
        organization = OrganizationModel("", "wughduuuw"),
        photoUris = listOf("", "", "", "", ""),
        rating = 4.7,
        deliveryCost = null,
        averageDeliveryTime = null,
        name = "Twihvwdihwdihv"
    ),
    val selectedImageIndex: Int = 0,
    val restaurantCategories: List<RestaurantCategoryModel> = listOf(
        RestaurantCategoryModel(
            "1",
            "Burgers"
        ),
        RestaurantCategoryModel(
            "2",
            "Pizza"
        ),
        RestaurantCategoryModel(
            "3",
            "Sushi"
        ),
        RestaurantCategoryModel(
            "4",
            "Salad"
        ),
        RestaurantCategoryModel(
            "5",
            "Sushi"
        ),
        RestaurantCategoryModel(
            "6",
            "Salad"
        ),
    ),
    val selectedCategoryId: String? = null,
    val selectedCategoryName: String? = null,
    val restaurantProducts: List<ProductModel> = listOf(
        ProductModel(
            productId = "1",
            title = "Product 1",
            description = "Some description this is super surger idk ",
            photoUrl = "",
            cost = 127.23f,
            costUnit = "Rub",
            count = 123,
            rating = 3.6f,
            categoryIds = listOf(),
            unit = 60,
            unitType = ProductUnitType.GRAM,
            enterpriseId = "",
            expiresAt = "",
            expiresDateType = ExpiresDateType.DAYS
        ),
        ProductModel(
            productId = "1",
            title = "Product 1",
            description = "Some description this is super surger idk ",
            photoUrl = "",
            cost = 127.23f,
            costUnit = "Rub",
            count = 123,
            rating = 3.6f,
            categoryIds = listOf(),
            unit = 60,
            unitType = ProductUnitType.GRAM,
            enterpriseId = "",
            expiresAt = "",
            expiresDateType = ExpiresDateType.DAYS
        ),
    )


)

