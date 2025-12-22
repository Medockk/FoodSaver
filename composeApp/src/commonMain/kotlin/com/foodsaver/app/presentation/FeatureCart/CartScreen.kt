@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.FeatureCart

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.foodsaver.app.common.PrimaryCenterAlignedTopAppBar
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.presentation.Cart.CartAction
import com.foodsaver.app.presentation.Cart.CartEvent
import com.foodsaver.app.presentation.Cart.CartState
import com.foodsaver.app.presentation.Cart.CartViewModel
import com.foodsaver.app.presentation.FeatureCart.components.CartProductCard
import com.foodsaver.app.presentation.routing.Route
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.cart
import foodsaver.composeapp.generated.resources.cart_location
import foodsaver.composeapp.generated.resources.ic_back_icon
import foodsaver.composeapp.generated.resources.time_to_delivery
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SharedTransitionScope.CartScreenRoot(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CartViewModel = koinViewModel(),
) {

    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is CartAction.OnError -> {
                snackbarHostState.showSnackbar(it.message)
            }

            is CartAction.OnProductClick -> {
                navController.navigate(
                    Route.MainGraph.ProductDetailScreen(
                        productId = it.productId,
                        isProductInCart = true
                    )
                )
            }
        }
    }

    CartScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Composable
private fun SharedTransitionScope.CartScreen(
    navController: NavController,
    state: CartState,
    onEvent: (CartEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            PrimaryCenterAlignedTopAppBar(
                title = stringResource(Res.string.cart),
                onNavigationButtonClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 30.dp),
        ) {

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.time_to_delivery),
                        color = MaterialTheme.colorScheme.primaryFixed,
                        fontSize = 15.sp
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "Сегодня 11:00 ",
                        color = MaterialTheme.colorScheme.inversePrimary
                    )

                    Icon(
                        painter = painterResource(Res.drawable.ic_back_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer {
                                rotationY = 180f
                            },
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.inverseSurface)
                        .padding(top = 20.dp, bottom = 10.dp)
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.cart_location),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp),
                        tint = Color.Unspecified
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(Modifier.weight(1f)) {
                        if (state.profile != null) {
                            state.profile?.currentCity?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.inverseOnSurface
                                )
                            }

                            val subTitle = (state.profile?.name ?: "") + " " + (state.profile?.phone ?: "")

                            if (subTitle.isNotBlank()) {
                                Text(
                                    text = subTitle,
                                    color = MaterialTheme.colorScheme.inversePrimary
                                )
                            }
                        } else {
                            with(this@Row) {
                                Box(Modifier.weight(0.25f).shimmerEffect())
                                Box(Modifier.weight(0.5f).shimmerEffect())
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            navController.navigate(Route.ProfileGraph.ProfileMenuScreen)
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .graphicsLayer {
                                    rotationY = 180f
                                },
                            tint = Color(0xFF4E60FF)
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .offset(y = (-10).dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                        .background(
                            MaterialTheme.colorScheme.background,
                            RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                        )
                ) {
                    Spacer(Modifier.height(30.dp))
                    state.cartProducts.forEach {
                        CartProductCard(
                            cartItem = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 15.dp),
                            onClick = {
                                navController.navigate(
                                    Route.MainGraph.ProductDetailScreen(
                                        productId = it.product.productId,
                                        isProductInCart = true
                                    )
                                )
                            },
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        Spacer(Modifier.height(20.dp))
                    }

                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}