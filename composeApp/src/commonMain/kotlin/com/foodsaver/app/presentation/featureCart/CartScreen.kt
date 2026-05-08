package com.foodsaver.app.presentation.featureCart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.featureCart.presentation.cart.CartEvent
import com.foodsaver.app.featureCart.presentation.cart.CartState
import com.foodsaver.app.featureCart.presentation.cart.CartViewModel
import com.foodsaver.app.presentation.featureCart.components.CartBottomBar
import com.foodsaver.app.presentation.featureCart.components.CartHeader
import com.foodsaver.app.presentation.featureCart.components.CartProductItem
import com.foodsaver.app.presentation.featureCart.components.CartProductItemState
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartScreenRoot(
    navController: NavController,
    viewModel: CartViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CartScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navController = navController
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CartScreenPreview() {
    LocalFoodSaverThemeComposition {
        CartScreen(
            navController = rememberNavController(),
            state = CartState(isItemsEditing = true),
            onEvent = {}
        )
    }
}

@Composable
private fun CartScreen(
    navController: NavController,
    state: CartState,
    onEvent: (CartEvent) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = FoodSaverTheme.colorScheme.backgroundContrast,
        bottomBar = {
            CartBottomBar(
                isDeliveryAddressEditing = state.isDeliveryAddressEditing,
                onDeliveryAddressEditingClick = { onEvent(CartEvent.OnEditDeliveryAddressClick) },
                deliveryAddressValue = state.deliveryAddress,
                onDeliveryAddressValueChange = { onEvent(CartEvent.OnAddressValueChange(it)) },
                totalPrice = state.totalCost,
                onBreakdownClick = { TODO() },
                onPlaceOrderClick = { TODO() },
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        topBar = {
            Column {
                val topBarPadding = WindowInsets.statusBars.asPaddingValues()
                CartHeader(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    isItemsEditing = state.isItemsEditing,
                    onEditButtonClick = {
                        onEvent(CartEvent.OnEditItemsClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(topBarPadding)
                )

                Spacer(Modifier.height(15.dp))
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(state.products) { product ->
                CartProductItem(
                    state = CartProductItemState(
                        productName = product.productName,
                        productPrice = product.productPrice,
                        productSize = "14 ''",
                        productImageUri = product.productImageUris,
                        isProductEditing = state.isItemsEditing,
                        productCount = product.quantityInCart,
                        onIncreaseClick = { onEvent(CartEvent.IncreaseProductClick(product)) },
                        onDecreaseClick = { onEvent(CartEvent.DecreaseProductClick(product)) },
                        onRemoveClick = { TODO() }
                    )
                )
            }
        }
    }
}