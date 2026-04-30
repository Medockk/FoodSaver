package com.foodsaver.app.presentation.featureFoodDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.common.restaurant.RestaurantSpecifications
import com.foodsaver.app.common.scaffold.ActionButtonItem
import com.foodsaver.app.common.scaffold.PrimaryScaffold
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.coreModel.model.ExpiresDateType
import com.foodsaver.app.coreModel.model.OrganizationModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreModel.model.ProductUnitType
import com.foodsaver.app.featureFoodDetail.domain.model.FoodIngredientModel
import com.foodsaver.app.featureFoodDetail.domain.model.FoodSizeModel
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailEvents
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailState
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailViewModel
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.RestaurantDetails
import com.foodsaver.app.presentation.featureFoodDetails.components.FoodDetailsBottomBar
import com.foodsaver.app.presentation.featureFoodDetails.components.IngredientView
import com.foodsaver.app.presentation.featureFoodDetails.components.SizeView
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.favorite_icon
import foodsaver.composeapp.generated.resources.ingredients
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

    FoodDetailsScreen(
        state = state,
        onEvents = viewModel::onEvent,
        navController = navController
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FoodDetailsScreenPreview() {
    LocalFoodSaverThemeComposition {
        FoodDetailsScreen(
            state = FoodDetailState(
                productImageUris = listOf("", "", ""),
                product = ProductModel(
                    productId = "",
                    title = "Some product",
                    description = "This is the best",
                    photoUrl = "",
                    cost = 128.0f,
                    costUnit = "$",
                    count = 26,
                    rating = 4.7f,
                    categoryIds = listOf(),
                    unit = 300,
                    unitType = ProductUnitType.GRAM,
                    enterpriseId = "",
                    expiresAt = "",
                    expiresDateType = ExpiresDateType.DAYS
                ),
                restaurantDetails = RestaurantDetails(
                    logoUri = ""
                ),
                restaurant = RestaurantModel(
                    id = "123",
                    name = "Bla bla bla",
                    description = "Some desc",
                    longitude = 23.3,
                    latitude = 43.3,
                    addressName = "Sowdowkd",
                    organization = OrganizationModel("", "Some org name"),
                    photoUris = listOf(),
                    rating = 4.5,
                    deliveryCost = null,
                    averageDeliveryTime = null,
                ),
                foodSizes = listOf(
                    FoodSizeModel("", "10”"),
                    FoodSizeModel("", "14”"),
                    FoodSizeModel("", "16”"),
                ),
                ingredients = listOf(
                    FoodIngredientModel(
                        "", "",
                        "Onion", true
                    ),
                    FoodIngredientModel(
                        "", "",
                        "Salt",
                    ),
                    FoodIngredientModel(
                        "", "",
                        "Pepper", true
                    ),
                    FoodIngredientModel(
                        "", "",
                        "Garlic",
                    ),
                    FoodIngredientModel(
                        "", "",
                        "Orange", true
                    ),
                    FoodIngredientModel(
                        "", "",
                        "Broccoli",
                    ),
                    FoodIngredientModel(
                        "", "",
                        "Ginger",
                    ),
                )
            ),
            onEvents = { TODO() },
            navController = rememberNavController()
        )
    }
}

@Composable
private fun FoodDetailsScreen(
    state: FoodDetailState,
    onEvents: (FoodDetailEvents) -> Unit,
    navController: NavController,
) {

    val productImagePagerState = rememberPagerState { state.productImageUris.size }
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    val buttonRowTopPadding = 15.dp
    val screenWidth = LocalWindowInfo.current.containerDpSize.width

    val maxImageHeight = 300.dp
    val minImageSize = 50.dp
    val scrollThresholdPx = retain(density) {
        with(density) {
            (maxImageHeight - minImageSize - buttonRowTopPadding).toPx()
        }
    }

    val scrollProgress by retain {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                1f
            } else {
                (listState.firstVisibleItemScrollOffset / scrollThresholdPx).coerceIn(0f, 1f)
            }
        }
    }

    LaunchedEffect(Unit) {
        println("Scroll progress $scrollProgress")
    }

    val currentHeight = lerp(maxImageHeight, minImageSize, scrollProgress)
    val currentWidth = lerp(screenWidth, minImageSize, scrollProgress)
    val cornerRadius = lerp(0.dp, minImageSize / 2, scrollProgress)
    val topPadding = lerp(0.dp, buttonRowTopPadding, scrollProgress)

    PrimaryScaffold(
        actionButton = {
            PrimaryFabButton(
                onClick = {
                    TODO()
                },
            ) {
                Icon(
                    imageVector = if (state.isFavoriteProduct) vectorResource(Res.drawable.selected_favorite_icon)
                    else vectorResource(Res.drawable.favorite_icon),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        },
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(currentHeight + topPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                HorizontalPager(
                    state = productImagePagerState,
                    modifier = Modifier
                        .size(currentWidth, currentHeight)
                        .clip(RoundedCornerShape(cornerRadius))
                ) { page ->
                    AsyncImageWithShimmerLoading(
                        model = state.productImageUris.getOrNull(page),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        },
        navigationButton = ActionButtonItem(
            icon = Res.drawable.back_icon,
            onClick = {
                navController.navigateUp()
            }
        ),
        bottomBar = {
            state.product?.let { product ->
                FoodDetailsBottomBar(
                    price = product.cost.toDouble(),
                    productCount = state.productCount.toInt(),
                    onIncreaseClick = {TODO()},
                    onDecreaseClick = {TODO()},
                    onAddProductToCart = {TODO()},
                    modifier = Modifier
                        .background(FoodSaverTheme.colorScheme.placeholderBackground)
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .background(FoodSaverTheme.colorScheme.background)
                .padding(bottom = paddingValues.calculateBottomPadding(), top = maxImageHeight + 25.dp,)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 24.dp
                )
            ) {
                // product name + restaurant logo
                item {
                    Spacer(Modifier.height(25.dp))
                    Text(
                        text = state.product?.title ?: "",
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
                    Spacer(Modifier.height(20.dp))
                    RestaurantSpecifications(
                        rating = state.restaurant?.rating,
                        deliveryCost = state.restaurant?.deliveryCost,
                        averageDeliveryTime = state.restaurant?.averageDeliveryTime,
                        modifier = Modifier.fillMaxWidth()
                    )
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
                                    onClick = {
                                        TODO()
                                    }
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
            }

        }
    }
}