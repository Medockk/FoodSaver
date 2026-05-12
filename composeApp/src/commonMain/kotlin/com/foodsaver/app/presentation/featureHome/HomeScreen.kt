package com.foodsaver.app.presentation.featureHome

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.searchField.SearchTextField
import com.foodsaver.app.common.searchField.SearchTextFieldState
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.Home.HomeEvent
import com.foodsaver.app.presentation.Home.HomeState
import com.foodsaver.app.presentation.Home.HomeViewModel
import com.foodsaver.app.presentation.featureHome.components.HomeTopBar
import com.foodsaver.app.presentation.featureHome.components.RestaurantCard
import com.foodsaver.app.presentation.featureHome.components.TableOfContent
import com.foodsaver.app.presentation.featureHome.components.category.CategoryChip
import com.foodsaver.app.presentation.featureHome.components.category.CategoryChipState
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.all
import foodsaver.composeapp.generated.resources.category_see_all
import foodsaver.composeapp.generated.resources.home_good_afternoon
import foodsaver.composeapp.generated.resources.home_hello_user
import foodsaver.composeapp.generated.resources.open_restaurants
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navController = navController
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    LocalFoodSaverThemeComposition {
        HomeScreen(
            HomeState(

            ), {}, rememberNavController()
        )
    }
}

@Composable
private fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    navController: NavController,
) {

    val shouldShowCategorySkeletonCards by retain(state.categories, state.isCategoriesLoading) {
        val value = state.isCategoriesLoading && state.categories.isEmpty()
        mutableStateOf(value)
    }
    val shouldShowRestaurantSkeletonCards by retain(state.restaurants, state.isRestaurantsLoading) {
        val value = state.restaurants.isEmpty() && state.isRestaurantsLoading
        mutableStateOf(value)
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize(),
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            // Top bar
            item {
                HomeTopBar(
                    deliverTo = state.deliverTo,
                    cartItemValue = state.cartSize,
                    onCartClick = {
                        navController.navigate(Route.CartGraph.CartScreen(state.cartId))
                    },
                    onMenuClick = { TODO() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }

            // Hello user text
            item {
                Spacer(Modifier.height(25.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = stringResource(
                            Res.string.home_hello_user,
                            state.profile?.name ?: ""
                        ),
                        color = FoodSaverTheme.colorScheme.onBackgroundSecondary,
                        style = FoodSaverTheme.typography.bodyRegular
                    )

                    Text(
                        text = stringResource(Res.string.home_good_afternoon),
                        color = FoodSaverTheme.colorScheme.onBackgroundSecondary,
                        style = FoodSaverTheme.typography.headerRegularBold
                    )
                }
                Spacer(Modifier.height(15.dp))
            }

            // search field
            // TODO make hero animation to search screen!!!
            item {
                SearchTextField(
                    state = SearchTextFieldState(
                        query = state.searchQuery,
                        onQueryChange = {
                            onEvent(HomeEvent.OnSearchQueryChange(it))
                        },
                        suggestion = "Hello World!",
                        onSearch = {},
                        enabled = false
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(horizontal = 24.dp)
                        .clickable {
                            navController.navigate(Route.MainGraph.SearchScreen())
                        }
                )
            }

            // category title + the list of categories
            item {
                Spacer(Modifier.height(30.dp))
                TableOfContent(
                    onSeeAllClick = {
                        TODO("Navigate to somewhere...")
                    },
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    text = Res.string.category_see_all
                )
                Spacer(Modifier.height(20.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    // all categories chip
                    item {
                        if (state.selectedCategoryIds.isEmpty()) {
                            CategoryChip(
                                state = CategoryChipState(
                                    name = stringResource(Res.string.all),
                                    imageUri = "",
                                    isMainChip = true,
                                    onCategoryClick = {
                                        navController.navigate(Route.MainGraph.SearchScreen())
                                    }
                                )
                            )
                        }
                    }

                    if (shouldShowCategorySkeletonCards) {
                        items(5) {
                            Box(
                                Modifier.widthIn(120.dp).heightIn(60.dp).clip(CircleShape)
                                    .shimmerEffect()
                            )
                        }
                    } else {
                        items(state.categories) { category ->
                            CategoryChip(
                                state = CategoryChipState(
                                    name = category.categoryName,
                                    imageUri = "",
                                    isMainChip = state.selectedCategoryIds.contains(category.categoryId),
                                    onCategoryClick = {
                                        navController.navigate(Route.MainGraph.SearchScreen(
                                            searchCategoryId = category.categoryId,
                                            categoryName = category.categoryName
                                        ))
                                    }
                                ),
                                modifier = Modifier
                                    .heightIn(min = 60.dp)
                            )
                        }
                    }
                }
            }

            // restaurants title
            item {
                Spacer(Modifier.height(30.dp))
                TableOfContent(
                    onSeeAllClick = {
                        TODO("Navigate to somewhere...")
                    },
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    text = Res.string.open_restaurants
                )
                Spacer(Modifier.height(20.dp))
            }

            // TODO add transition animation
            // restaurants skeleton shimmers
            if (shouldShowRestaurantSkeletonCards) {
                items(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(170.dp)
                            .padding(horizontal = 25.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .shimmerEffect()
                    )

                    Spacer(Modifier.height(25.dp))
                }
            } else {
                items(state.restaurants) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onRestaurantClick = {
                            navController.navigate(Route.MainGraph.Restaurant(restaurant.id, restaurant.name))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                    Spacer(Modifier.height(25.dp))
                }
            }
        }
    }
}