@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.featureSearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.product.AddProductCard
import com.foodsaver.app.common.product.ProductCard
import com.foodsaver.app.common.textField.searchField.SearchTextField
import com.foodsaver.app.common.textField.searchField.SearchTextFieldState
import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.featureSearch.domain.model.ProductCardModel
import com.foodsaver.app.featureSearch.presentation.search.SearchEvent
import com.foodsaver.app.featureSearch.presentation.search.SearchState
import com.foodsaver.app.featureSearch.presentation.search.SearchViewModel
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureHome.components.RestaurantCard
import com.foodsaver.app.presentation.featureSearch.components.RecentKeywordsList
import com.foodsaver.app.presentation.featureSearch.components.SearchTopBar
import com.foodsaver.app.presentation.featureSearch.components.SuggestedRestaurants
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.open_restaurants
import foodsaver.composeapp.generated.resources.popular_food
import foodsaver.composeapp.generated.resources.popular_item
import foodsaver.composeapp.generated.resources.products
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun SearchScreenRoot(
    navController: NavController,
    viewModel: SearchViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
    LocalFoodSaverThemeComposition {
        SearchScreen(
            navController = rememberNavController(),
            state = SearchState(
                popularFood = listOf(
                    ProductModel(
                        productId = "k",
                        name = "First product",
                        description = "Description of first product",
                        imageUris = listOf(),
                        expiresAt = Clock.System.now(),
                        price = 120.0,
                        discount = 0.0,
                        count = 14,
                        unit = "kg",
                        currency = "$",
                        restaurantId = "",
                        categoryIds = listOf(),
                        ingredientIds = listOf(),
                        isDeleted = false,
                        isAvailable = true
                    ),
                    ProductModel(
                        productId = "k",
                        name = "Second product",
                        description = "Description of the second product",
                        imageUris = listOf(),
                        expiresAt = Clock.System.now(),
                        price = 120.0,
                        discount = 0.0,
                        count = 14,
                        unit = "kg",
                        currency = "$",
                        restaurantId = "",
                        categoryIds = listOf(),
                        ingredientIds = listOf(),
                        isDeleted = false,
                        isAvailable = true
                    ),
                    ProductModel(
                        productId = "k",
                        name = "Third product",
                        description = "Description of third product",
                        imageUris = listOf(),
                        expiresAt = Clock.System.now(),
                        price = 120.0,
                        discount = 0.0,
                        count = 14,
                        unit = "kg",
                        currency = "$",
                        restaurantId = "",
                        categoryIds = listOf(),
                        ingredientIds = listOf(),
                        isDeleted = false,
                        isAvailable = true
                    ),
                    ProductModel(
                        productId = "k",
                        name = "Fourth product",
                        description = "Description of fourth product",
                        imageUris = listOf(),
                        expiresAt = Clock.System.now(),
                        price = 120.0,
                        discount = 0.0,
                        count = 14,
                        unit = "kg",
                        currency = "$",
                        restaurantId = "",
                        categoryIds = listOf(),
                        ingredientIds = listOf(),
                        isDeleted = false,
                        isAvailable = true
                    ),
                ),
                suggestedProducts = listOf(
                    getDumbProductCardModel(),
                    getDumbProductCardModel(),
                    getDumbProductCardModel(),
                    getDumbProductCardModel(),
                ),
                searchedProducts = listOf(
                    ProductModel(
                        productId = "a",
                        name = "First",
                        description = "Demkmkfb fiue",
                        imageUris = listOf(),
                        expiresAt = Clock.System.now(),
                        price = 120.0,
                        discount = 0.0,
                        count = 34,
                        unit = "kg",
                        currency = "$",
                        restaurantId = "",
                        categoryIds = listOf(),
                        ingredientIds = listOf(),
                        isDeleted = false,
                        isAvailable = true
                    ),
                    ProductModel(
                        productId = "a",
                        name = "Second",
                        description = "Demkmkfb fiue",
                        imageUris = listOf(),
                        expiresAt = Clock.System.now(),
                        price = 120.0,
                        discount = 0.0,
                        count = 34,
                        unit = "kg",
                        currency = "$",
                        restaurantId = "",
                        categoryIds = listOf(),
                        ingredientIds = listOf(),
                        isDeleted = false,
                        isAvailable = true
                    ),
                    ProductModel(
                        productId = "a",
                        name = "Third",
                        description = "Demkmkfb fiue",
                        imageUris = listOf(),
                        expiresAt = Clock.System.now(),
                        price = 120.0,
                        discount = 0.0,
                        count = 34,
                        unit = "kg",
                        currency = "$",
                        restaurantId = "",
                        categoryIds = listOf(),
                        ingredientIds = listOf(),
                        isDeleted = false,
                        isAvailable = true
                    ),
                    ProductModel(
                        productId = "a",
                        name = "Fourth",
                        description = "Demkmkfb fiue",
                        imageUris = listOf(),
                        expiresAt = Clock.System.now(),
                        price = 120.0,
                        discount = 0.0,
                        count = 34,
                        unit = "kg",
                        currency = "$",
                        restaurantId = "",
                        categoryIds = listOf(),
                        ingredientIds = listOf(),
                        isDeleted = false,
                        isAvailable = true
                    ),
                ),
                openRestaurants = listOf(
                    RestaurantModel(
                        id = "qwe",
                        name = "First restaurant",
                        description = "This is my first restaurant",
                        longitude = 0.0,
                        latitude = 9.9,
                        addressName = "qqwe",
                        companyId = "d"
                    ),
                    RestaurantModel(
                        id = "qwe",
                        name = "First restaurant",
                        description = "This is my first restaurant",
                        longitude = 0.0,
                        latitude = 9.9,
                        addressName = "qqwe",
                        companyId = "d",
                        rating = 4.8,
                    ),
                    RestaurantModel(
                        id = "qwe",
                        name = "First restaurant",
                        description = "This is my first restaurant",
                        longitude = 0.0,
                        latitude = 9.9,
                        addressName = "qqwe",
                        companyId = "d",
                        rating = 4.7
                    ),
                ),
                isFirstSearchingScreen = true,
                query = TextFieldValue("Burger")
            ),
            onEvent = {
                /*TODO()*/
            }
        )
    }
}

private fun getDumbProductCardModel() = ProductCardModel(
    productId = "",
    restaurantName = "",
    name = "",
    imageUri = ""
)
private fun getDumbSuggestedRestaurant() = RestaurantModel(
    id = "",
    name = "",
    description = "",
    longitude = 0.0,
    latitude = 0.0,
    addressName = "",
    companyId = "",
    photoUris = listOf("")
)

@Composable
private fun SearchScreen(
    navController: NavController,
    state: SearchState,
    onEvent: (SearchEvent) -> Unit,
) {

    val columnValues = 2
    val fullSpan: LazyGridItemSpanScope.() -> GridItemSpan = {
        GridItemSpan(columnValues)
    }

    Scaffold(
        contentWindowInsets = WindowInsets.statusBars,
        modifier = Modifier
            .fillMaxSize(),
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            Column {
                SearchTopBar(
                    navController = navController,
                    isFirstSearchingScreen = state.isFirstSearchingScreen,
                    cartItemValue = state.cartItems,
                    onCartIconClick = {
                        navController.navigate(Route.CartGraph.CartScreen(state.cartId))
                    },
                    searchQuery = state.query.text,
                    onShowMoreSearchVariantsClick = {
                        /*TODO()*/
                    },
                    onSearchIconClick = {
                        onEvent(SearchEvent.OnSearchIconClick)
                    },
                    onFilterClick = {
                        onEvent(SearchEvent.OnFilterClick)
                    }
                )
            }
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnValues),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // first searching screen
            if (state.isFirstSearchingScreen) {
                // search field
                item(span = fullSpan) {
                    Column {
                        Spacer(Modifier.height(25.dp))

                        SearchTextField(
                            state = SearchTextFieldState(
                                query = state.query,
                                onQueryChange = { onEvent(SearchEvent.OnQueryChange(it)) },
                                suggestion = state.suggestion,
                                onSearch = {
                                    onEvent(SearchEvent.OnSearch(it.text))
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 60.dp)
                                .padding(horizontal = 24.dp)
                        )
                    }
                }

                // recent keywords
                item(span = fullSpan) {
                    if (state.recentKeywords.isNotEmpty()) {
                        Column {
                            Spacer(Modifier.height(25.dp))
                            RecentKeywordsList(
                                keywords = state.recentKeywords,
                                onKeyworkClick = { word ->
                                    onEvent(SearchEvent.OnRecentKeywordsClick(word))
                                }
                            )
                        }
                    }
                }

                // suggested restaurants
                item(span = fullSpan) {
                    AnimatedVisibility(
                        visible = state.suggestedRestaurants.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        SuggestedRestaurants(
                            restaurants = state.suggestedRestaurants,
                            onRestaurantClick = { restaurant ->
                                navController.navigate(
                                    Route.MainGraph.Restaurant(
                                        restaurantId = restaurant.id,
                                        restaurantName = restaurant.name
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .padding(top = 30.dp)
                        )
                    }
                    AnimatedVisibility(
                        visible = state.suggestedRestaurants.isEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        val dumbRestaurant = listOf(
                            getDumbSuggestedRestaurant(),
                            getDumbSuggestedRestaurant(),
                            getDumbSuggestedRestaurant(),
                        )
                        SuggestedRestaurants(
                            dumbRestaurant,
                            {},
                            Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }

                // popular food
                item(span = fullSpan) {
                    Column {
                        Text(
                            text = stringResource(Res.string.popular_food),
                            style = FoodSaverTheme.typography.bodyMedium,
                            color = FoodSaverTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .padding(top = 15.dp)
                        )
                        Spacer(Modifier.height(30.dp))
                    }
                }
                if (state.suggestedProducts.isEmpty()) {
                    val dumbProducts = getDumbProductCardModel()
                    items(6) { index ->
                        val isLeftItem = index % 2 == 0
                        ProductCard(
                            state = dumbProducts,
                            onProductClick = {

                            },
                            modifier = Modifier
                                .padding(
                                    start = if (isLeftItem) 24.dp else 0.dp,
                                    end = if (isLeftItem) 0.dp else 24.dp,
                                    bottom = 15.dp
                                )
                        )
                    }
                } else {
                    itemsIndexed(state.suggestedProducts) { index, product ->
                        val isLeftItem = index % 2 == 0
                        val cartItem =
                            state.searchedProductCartItemIds.find { it.productId == product.productId }
                        ProductCard(
                            state = product,
                            onProductClick = {
                                navController.navigate(
                                    Route.MainGraph.FoodDetailsScreen(
                                        productId = product.productId,
                                        productName = product.name,
                                        productCartItemId = cartItem?.cartItemId
                                    )
                                )
                            },
                            modifier = Modifier
                                .padding(
                                    start = if (isLeftItem) 24.dp else 0.dp,
                                    end = if (isLeftItem) 0.dp else 24.dp,
                                    bottom = 15.dp
                                )
                        )
                    }
                }
            }
            // second searching screen
            else {
                item(span = fullSpan) {
                    Column {
                        Spacer(Modifier.height(25.dp))
                        val formatArgs = state.selectedCategory?.categoryName
                            ?: stringResource(Res.string.products)
                        Text(
                            text = stringResource(
                                Res.string.popular_item,
                                formatArgs
                            ),
                            style = FoodSaverTheme.typography.bodyMedium,
                            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                        )
                        Spacer(Modifier.height(25.dp))
                    }
                }

                itemsIndexed(state.searchedProducts) { index, product ->
                    val isLeftItem = index % 2 == 0
                    val cartItem =
                        state.searchedProductCartItemIds.find { it.productId == product.productId }
                    AddProductCard(
                        product = product,
                        onProductClick = {
                            navController.navigate(
                                Route.MainGraph.FoodDetailsScreen(
                                    productId = product.productId,
                                    productName = product.name,
                                    productCartItemId = cartItem?.cartItemId
                                )
                            )
                        },
                        isProductInCart = false,
                        onAddClick = {},
                        onRemoveClick = {},
                        modifier = Modifier
                            .padding(
                                start = if (isLeftItem) 24.dp else 0.dp,
                                end = if (isLeftItem) 0.dp else 24.dp,
                                bottom = 15.dp
                            )
                    )
                }

                item(span = fullSpan) {
                    Column {
                        Spacer(Modifier.height(30.dp))
                        Text(
                            text = stringResource(Res.string.open_restaurants),
                            style = FoodSaverTheme.typography.bodyMedium,
                            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                        )
                        Spacer(Modifier.height(15.dp))
                    }
                }
                items(
                    items = state.openRestaurants,
                    span = {
                        fullSpan()
                    }
                ) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onRestaurantClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}