@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.FeatureProduct

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.domain.model.ExpiresDateType
import com.foodsaver.app.domain.model.ProductUnitType
import com.foodsaver.app.presentation.FeatureProduct.components.ProductCounter
import com.foodsaver.app.presentation.ProductDetail.ProductDetailEvents
import com.foodsaver.app.presentation.ProductDetail.ProductDetailState
import com.foodsaver.app.presentation.ProductDetail.ProductDetailViewModel
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_product_to_cart
import foodsaver.composeapp.generated.resources.days
import foodsaver.composeapp.generated.resources.hours
import foodsaver.composeapp.generated.resources.ic_back_icon
import foodsaver.composeapp.generated.resources.ic_clock_icon
import foodsaver.composeapp.generated.resources.ic_heart_icon
import foodsaver.composeapp.generated.resources.ic_kilogram_icon
import foodsaver.composeapp.generated.resources.ic_liters_icon
import foodsaver.composeapp.generated.resources.ic_rating_icon
import foodsaver.composeapp.generated.resources.total_items
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SharedTransitionScope.ProductScreenRoot(
    productId: String,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: ProductDetailViewModel = koinViewModel(parameters = {
        parametersOf(productId)
    }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProductScreen(
        productId = productId,
        state = state,
        navController = navController,
        animatedVisibilityScope = animatedVisibilityScope,
        onEvent = viewModel::onEvent
    )
}


@Composable
private fun SharedTransitionScope.ProductScreen(
    productId: String,
    state: ProductDetailState,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onEvent: (ProductDetailEvents) -> Unit,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back_icon),
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_heart_icon),
                            contentDescription = "Make fav",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.product?.let { product ->
                Spacer(Modifier.height(80.dp))

                SubcomposeAsyncImage(
                    model = product.photoUrl,
                    contentDescription = null,
                    loading = {
                        Box(Modifier.fillMaxSize().shimmerEffect())
                    },
                    success = {
                        SubcomposeAsyncImageContent(Modifier.fillMaxWidth())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_image_${product.productId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween()
                            }
                        ),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    text = product.name,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_name_${product.productId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(450, easing = LinearEasing)
                            },
                            renderInOverlayDuringTransition = true
                        ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.primaryFixed
                )
                Text(
                    text = "${product.costUnit} ${product.cost}",
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_cost_${product.productId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween()
                            }
                        ),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(30.dp))

                ProductCounter(
                    count = state.productCount,
                    onIncreaseClick = {
                        onEvent(ProductDetailEvents.OnIncreaseCountClick)
                    },
                    onDecreaseClick = {
                        onEvent(ProductDetailEvents.OnDecreaseCountClick)
                    }
                )


                Spacer(Modifier.height(30.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_rating_icon),
                        contentDescription = "rating",
                        modifier = Modifier
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = product.rating?.toString() ?: "0",
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(Modifier.weight(1f))

                    Row(
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("product_unit_${product.productId}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween()
                                }
                            ),
                    ) {
                        Icon(
                            painter = painterResource(
                                resource = when (product.unitType) {
                                    ProductUnitType.KILOGRAM, ProductUnitType.GRAM -> Res.drawable.ic_kilogram_icon
                                    ProductUnitType.LITERS, ProductUnitType.MILLILITERS -> Res.drawable.ic_liters_icon
                                }
                            ),
                            contentDescription = "liters",
                            modifier = Modifier
                                .size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "${product.unit} ${product.unitType.value}",
                            color = MaterialTheme.colorScheme.inversePrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(Modifier.weight(1f))

                    Row(
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("product_expiresAt_${product.productId}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween()
                                }
                            ),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_clock_icon),
                            contentDescription = "expires at",
                            modifier = Modifier
                                .size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(5.dp))
                        val dateType = when (product.expiresDateType) {
                            ExpiresDateType.DAYS -> stringResource(Res.string.days)
                            ExpiresDateType.HOURS -> stringResource(Res.string.hours)
                        }

                        Text(
                            text = "${product.expiresAt} $dateType",
                            color = MaterialTheme.colorScheme.inversePrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Spacer(Modifier.height(15.dp))

                Text(
                    text = product.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_desc_${productId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween()
                            }
                        ),
                    maxLines = 4,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primaryFixed,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = 15.sp,
                        maxFontSize = 25.sp
                    )
                )
                Spacer(Modifier.height(20.dp))

                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 20.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${stringResource(Res.string.total_items)} x${state.productCount}",
                            color = MaterialTheme.colorScheme.inversePrimary,
                            fontSize = 15.sp
                        )

                        Text(
                            text = "${product.costUnit}${product.cost}",
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = {
                            onEvent(ProductDetailEvents.AddProductToCart)
                        },
                        modifier = Modifier
                            .heightIn(min = 55.dp)
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("product_btn_${product.productId}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween()
                                },
                                renderInOverlayDuringTransition = false
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(7.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.add_product_to_cart),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                            maxLines = 1
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}
