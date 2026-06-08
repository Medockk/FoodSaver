package com.foodsaver.app.presentation.featureFoodDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.image.ImagePageIndicator
import com.foodsaver.app.common.image.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.common.button.PrimaryTextButton
import com.foodsaver.app.common.topBar.collapsingToolbar.CollapsingToolbarImage
import com.foodsaver.app.common.topBar.collapsingToolbar.rememberToolbarScrollBehavior
import com.foodsaver.app.common.topBar.collapsingToolbar.rememberToolbarScrollState
import com.foodsaver.app.common.ingredient.IngredientView
import com.foodsaver.app.common.restaurant.RestaurantSpecifications
import com.foodsaver.app.common.shape.interpolate
import com.foodsaver.app.featureFoodDetail.domain.model.IngredientAnalyzeResponse
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailActions
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailEvents
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailState
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailViewModel
import com.foodsaver.app.presentation.featureFoodDetails.components.FoodDetailsBottomBar
import com.foodsaver.app.presentation.featureFoodDetails.components.ingredientClassificationView
import com.foodsaver.app.presentation.featureFoodDetails.components.SizeView
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.analyze_ingredients
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.favorite_icon
import foodsaver.composeapp.generated.resources.ingredients
import foodsaver.composeapp.generated.resources.product_available_count
import foodsaver.composeapp.generated.resources.selected_favorite_icon
import foodsaver.composeapp.generated.resources.size
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FoodDetailsScreenRoot(
    navController: NavController,
    viewModel: FoodDetailViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val aiIngredientsState by viewModel.aiIngredientsResponse.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    FoodDetailsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navController = navController,
        onRefresh = viewModel::onRefresh,
        aiIngredientsState = aiIngredientsState,
        snackbarHostState = snackbarHostState
    )

    ObserveActions(viewModel.channel) {
        when (it) {
            FoodDetailActions.OnAddedToCart -> {}
            is FoodDetailActions.OnError -> {snackbarHostState.showSnackbar(it.message, withDismissAction = true)}
        }
    }
}

@Composable
private fun FoodDetailsScreen(
    state: FoodDetailState,
    aiIngredientsState: List<IngredientAnalyzeResponse>,
    onEvent: (FoodDetailEvents) -> Unit,
    onRefresh: () -> Unit,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val toolbarScrollState = rememberToolbarScrollState()
    val scrollBehavior = rememberToolbarScrollBehavior(toolbarScrollState)
    var isImageCollapsed by retain { mutableStateOf(true) }

    val shimmerColor = FoodSaverTheme.colorScheme.shimmerColor
    var dominantImageColor by retain { mutableStateOf(shimmerColor) }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isImageCollapsed) FoodSaverTheme.colorScheme.background
        else dominantImageColor,
        animationSpec = tween()
    )

    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            CollapsingToolbarImage(
                collapsedElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(animatedBackgroundColor)
                    .statusBarsPadding(),
                navigationIcon = {
                    PrimaryFabButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.back_icon),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(FoodSaverTheme.colorScheme.background)
                                .padding(5.dp)
                        ) {
                            Text(
                                text = state.expiresTime,
                                style = FoodSaverTheme.typography.bodyRegular,
                                color = FoodSaverTheme.colorScheme.onBackground
                            )
                        }
                        Spacer(Modifier.width(5.dp))
                        PrimaryFabButton(
                            onClick = { onEvent(FoodDetailEvents.OnFavoriteStatusChange) }
                        ) {
                            Icon(
                                imageVector = if (state.isFavoriteProduct) vectorResource(Res.drawable.selected_favorite_icon)
                                else vectorResource(Res.drawable.favorite_icon),
                                contentDescription = null
                            )
                        }
                    }
                },
                collapsingImage = { progress ->

                    isImageCollapsed = progress > 0.85f

                    val shouldShowImagePageIndicator = progress < 0.15f
                    val shape = RoundedCornerShape(
                        lerp(
                            0f, 100f, progress
                        ).toInt()
                    )

                    Box(
                        modifier = Modifier
                            .padding(bottom = if (isImageCollapsed) 3.dp else 15.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isImageCollapsed) dominantImageColor
                                    else animatedBackgroundColor, CircleShape
                                )
                        ) {
                            AsyncImageWithShimmerLoading(
                                model = state.product?.imageUris,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxSize()
                                    .clip(shape),
                                onPageChange = { index ->
                                    onEvent(FoodDetailEvents.OnChangeSelectedImageIndex(index))
                                },
                                onColorGenerated = { color ->
                                    dominantImageColor = color
                                },
                                contentScale = ContentScale.Fit.interpolate()
                            )
                        }

                        state.product?.imageUris?.let { imageUris ->
                            AnimatedVisibility(
                                visible = shouldShowImagePageIndicator,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                ImagePageIndicator(
                                    items = imageUris.size,
                                    currentPosition = state.selectedImageIndex
                                )
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            val bottomBarPaddingValues = WindowInsets.navigationBars.asPaddingValues()

            state.product?.let { product ->
                FoodDetailsBottomBar(
                    price = product.price,
                    discount = product.discount,
                    productCount = state.productCount,
                    onIncreaseClick = { onEvent(FoodDetailEvents.OnIncreaseCountClick) },
                    onDecreaseClick = { onEvent(FoodDetailEvents.OnDecreaseCountClick) },
                    onAddProductToCart = { onEvent(FoodDetailEvents.OnAddProductToCart) },
                    onRemoveProductFromCart = { onEvent(FoodDetailEvents.OnRemoveProductFromCart) },
                    isProductInCart = state.isProductInCart,
                    modifier = Modifier
                        .background(FoodSaverTheme.colorScheme.placeholderBackground)
                        .padding(bottomBarPaddingValues)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = FoodSaverTheme.colorScheme.background
    ) { paddingValues ->

        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = state.isRefresh,
            onRefresh = onRefresh,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = state.isRefresh,
                    modifier = Modifier.padding(paddingValues).align(Alignment.TopCenter),
                    color = FoodSaverTheme.colorScheme.primary,
                    containerColor = FoodSaverTheme.colorScheme.placeholderBackground
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = 24.dp
                    )
                )
                {
                    // product name + restaurant logo
                    item {
                        Spacer(Modifier.height(25.dp))
                        Text(
                            text = state.productName,
                            style = FoodSaverTheme.typography.bodyBold,
                            color = FoodSaverTheme.colorScheme.onBackground
                        )

                        state.restaurant?.let { restaurant ->
                            Spacer(Modifier.height(7.dp))
                            Row {
                                state.restaurantDetails?.let { restaurantDetails ->
                                    // TODO add restaurant logo
                                    AsyncImageWithShimmerLoading(
                                        model = restaurantDetails.logoUri,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(Modifier.width(10.dp))
                                }

                                Text(
                                    text = restaurant.name,
                                    color = FoodSaverTheme.colorScheme.onBackground,
                                    style = FoodSaverTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    // restaurant specifications
                    item {
                        state.restaurant?.let {
                            Spacer(Modifier.height(20.dp))
                            RestaurantSpecifications(
                                rating = state.restaurant?.rating,
                                deliveryCost = state.restaurant?.deliveryCost,
                                averageDeliveryTime = state.restaurant?.averageDeliveryTime,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // food description
                    item {
                        Spacer(Modifier.height(20.dp))
                        Text(
                            text = state.product?.description ?: "",
                            style = FoodSaverTheme.typography.bodyRegular,
                            color = FoodSaverTheme.colorScheme.onBackgroundTertiary,
                            textAlign = TextAlign.Justify
                        )
                    }

                    item {
                        if (state.product != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(Res.string.product_available_count) + ":",
                                    color = FoodSaverTheme.colorScheme.onBackground,
                                    style = FoodSaverTheme.typography.bodyRegular
                                )
                                Spacer(Modifier.width(5.dp))
                                println("Product ${state.product}")
                                println("Product count ${state.product?.count}")
                                Text(
                                    text = state.product?.count.toString(),
                                    color = FoodSaverTheme.colorScheme.onBackground,
                                    style = FoodSaverTheme.typography.bodyRegular
                                )
                            }
                        }
                    }

                    // food sizes
                    item {
                        Spacer(Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(Res.string.size).uppercase(),
                                color = FoodSaverTheme.colorScheme.onBackgroundTertiary,
                                style = FoodSaverTheme.typography.bodyRegular
                            )
                            Spacer(Modifier.width(10.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                itemsIndexed(state.foodSizes) { index, size ->
                                    SizeView(
                                        size = size.size,
                                        isSelected = index == state.selectedSizeIndex,
                                        onClick = { /*TODO()*/ }
                                    )
                                }
                            }
                        }
                    }

                    // ingredients
                    item {
                        Spacer(Modifier.height(20.dp))

                        Text(
                            text = stringResource(Res.string.ingredients).uppercase(),
                            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
                            style = FoodSaverTheme.typography.bodyRegular
                        )

                        Spacer(Modifier.height(20.dp))

                        AnimatedVisibility(state.ingredients.isNotEmpty()) {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(15.dp),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                state.ingredients.forEach { ingredient ->
                                    IngredientView(
                                        ingredient = ingredient
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))

                        AnimatedVisibility(
                            visible = !state.isAiResponseCompleted,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            PrimaryTextButton(
                                onClick = { onEvent(FoodDetailEvents.OnAnalyzeIngredients) },
                                enabled = !state.isAiResponseLoading,
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Box {
                                    androidx.compose.animation.AnimatedVisibility(state.isAiResponseLoading) {
                                        CircularProgressIndicator(color = FoodSaverTheme.colorScheme.primary)
                                    }

                                    androidx.compose.animation.AnimatedVisibility(!state.isAiResponseLoading) {
                                        Text(
                                            text = stringResource(Res.string.analyze_ingredients),
                                            color = FoodSaverTheme.colorScheme.primary,
                                            style = FoodSaverTheme.typography.bodyMedium,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    }
                                }
                            }
                        }
                    }

                    ingredientClassificationView(
                        classifications = aiIngredientsState,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}