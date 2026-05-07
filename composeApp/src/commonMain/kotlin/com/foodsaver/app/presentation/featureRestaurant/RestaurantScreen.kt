package com.foodsaver.app.presentation.featureRestaurant

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.common.chip.PrimaryChip
import com.foodsaver.app.common.product.AddProductCard
import com.foodsaver.app.common.scaffold.ActionButtonItem
import com.foodsaver.app.common.scaffold.PrimaryScaffold
import com.foodsaver.app.featureEnterprises.presentation.enterprises.RestaurantState
import com.foodsaver.app.featureEnterprises.presentation.enterprises.RestaurantEvent
import com.foodsaver.app.featureEnterprises.presentation.enterprises.RestaurantViewModel
import com.foodsaver.app.common.ImagePageIndicator
import com.foodsaver.app.common.restaurant.RestaurantSpecifications
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.more_icon
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RestaurantScreenRoot(
    navController: NavController,
    viewModel: RestaurantViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    RestaurantScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navController = navController
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RestaurantScreenPreview() {
    LocalFoodSaverThemeComposition {
        RestaurantScreen(
            state = RestaurantState(
                selectedImageIndex = 3,
                selectedCategoryId = "1",
            ),

            onEvent = { TODO() },
            navController = rememberNavController()
        )
    }
}

@Composable
private fun RestaurantScreen(
    state: RestaurantState,
    onEvent: (RestaurantEvent) -> Unit,
    navController: NavController,
) {

    val restaurantImagePager = rememberPagerState { state.restaurant?.photoUris?.size ?: 1 }
    val shouldShowProductsSkeleton by retain(state.restaurantProducts, state.isProductsLoading) {
        val value = state.restaurantProducts.isEmpty() && state.isProductsLoading
        mutableStateOf(value)
    }

    PrimaryScaffold(
        actionButton = ActionButtonItem(
            onClick = {
                TODO()
            },
            icon = Res.drawable.more_icon
        ),
        bottomBackgroundContent = {
            ImagePageIndicator(
                items = state.restaurant?.photoUris?.size ?: 0,
                currentPosition = state.selectedImageIndex,
            )
        },
        navigationButton = ActionButtonItem(
            onClick = {
                navController.navigateUp()
            },
            icon = Res.drawable.back_icon
        ),
        backgroundContent = {

            LaunchedEffect(restaurantImagePager.currentPage) {
                onEvent(RestaurantEvent.OnSelectedImageIndexChange(restaurantImagePager.currentPage))
            }

            HorizontalPager(
                state = restaurantImagePager
            ) { page ->
                AsyncImageWithShimmerLoading(
                    model = state.restaurant?.photoUris?.getOrNull(page),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(300.dp)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .background(FoodSaverTheme.colorScheme.background)
                .padding(top = paddingValues.calculateTopPadding()),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // specifications
            item(
                span = { GridItemSpan(2) }
            ) {
                Column {
                    Spacer(Modifier.height(25.dp))
                    if (state.restaurant != null) {
                        RestaurantSpecifications(
                            rating = state.restaurant?.rating,
                            deliveryCost = state.restaurant?.deliveryCost,
                            averageDeliveryTime = state.restaurant?.averageDeliveryTime,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }
                }
            }

            // name + description
            item(
                span = { GridItemSpan(2) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .animateContentSize()
                ) {
                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = state.restaurantName,
                        color = FoodSaverTheme.colorScheme.onBackground,
                        style = FoodSaverTheme.typography.bodyBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.restaurant?.description ?: "",
                        color = FoodSaverTheme.colorScheme.onBackgroundTertiary,
                        style = FoodSaverTheme.typography.bodyRegular,
                        textAlign = TextAlign.Justify
                    )
                }
            }

            // categories
            item(
                span = { GridItemSpan(2) }
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.restaurantCategories) { category ->
                        PrimaryChip(
                            text = category.name,
                            isSelected = state.selectedCategoryId == category.id,
                            onClick = { TODO() }
                        )
                    }
                }
            }

            // selected category
            item(
                span = { GridItemSpan(2) }
            ) {
                state.selectedCategoryName?.let { selectedCategoryName ->
                    Spacer(Modifier.height(30.dp))
                    Text(
                        text = selectedCategoryName,
                        style = FoodSaverTheme.typography.bodyMedium,
                        color = FoodSaverTheme.colorScheme.onBackground
                    )
                }
                Spacer(Modifier.height(15.dp))
            }

            // product by category
            if (shouldShowProductsSkeleton) {
                items(6) { index ->
                    val isLeftColumn = index % 2 == 0

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = if (isLeftColumn) 24.dp else 0.dp,
                                end = if (isLeftColumn) 0.dp else 24.dp,
                                bottom = 15.dp
                            )
                            .height(180.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .shimmerEffect()
                    )
                }
            } else {
                itemsIndexed(state.restaurantProducts) { index, product ->

                    val isLeftColumn = index % 2 == 0

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = if (isLeftColumn) 24.dp else 0.dp,
                                end = if (isLeftColumn) 0.dp else 24.dp,
                            )
                    ) {
                        AddProductCard(
                            product = product,
                            isProductInCart = false,
                            onAddClick = { TODO() },
                            onRemoveClick = { TODO() },
                            onProductClick = {
                                navController.navigate(Route.HomeGraph.FoodDetailsScreen(
                                    product.productId,
                                    product.name,
                                    false // TODO
                                ))
                            },
                        )
                    }
                }
            }

        }
    }
}