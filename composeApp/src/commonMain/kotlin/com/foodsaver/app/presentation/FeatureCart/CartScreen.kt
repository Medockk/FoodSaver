package com.foodsaver.app.presentation.FeatureCart

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.presentation.Cart.CartAction
import com.foodsaver.app.presentation.Cart.CartEvent
import com.foodsaver.app.presentation.Cart.CartState
import com.foodsaver.app.presentation.Cart.CartViewModel
import com.foodsaver.app.presentation.FeatureCart.components.CartProductCard
import com.foodsaver.app.presentation.routing.Route
import com.foodsaver.app.utils.ObserveActions
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartScreenRoot(
    navController: NavController,
    viewModel: CartViewModel = koinViewModel()
) {

    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is CartAction.OnError -> {
                snackbarHostState.showSnackbar(it.message)
            }
            is CartAction.OnProductClick -> {
                navController.navigate(Route.MainGraph.ProductDetailScreen(
                    productId = it.productId,
                    isProductInCart = true
                ))
            }
        }
    }

    CartScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun CartScreen(
    navController: NavController,
    state: CartState,
    onEvent: (CartEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {

        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {

            items(state.cartProducts) { cartItem ->
                CartProductCard(
                    cartItem = cartItem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 15.dp)
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}