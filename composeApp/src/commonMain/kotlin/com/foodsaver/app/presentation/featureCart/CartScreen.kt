package com.foodsaver.app.presentation.featureCart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.featureCart.presentation.cart.CartEvent
import com.foodsaver.app.featureCart.presentation.cart.CartState
import com.foodsaver.app.featureCart.presentation.cart.CartViewModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.cart
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
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

@Composable
private fun CartScreen(
    navController: NavController,
    state: CartState,
    onEvent: (CartEvent) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier
            .fillMaxSize(),
        containerColor = FoodSaverTheme.colorScheme.backgroundContrast
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                PrimaryFabButton(
                    onClick = {
                        navController.navigateUp()
                    },
                    background = FoodSaverTheme.colorScheme.cartFabColor
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.back_icon),
                        contentDescription = null,
                        tint = FoodSaverTheme.colorScheme.onCartFabColor
                    )
                }
                Spacer(Modifier.width(20.dp))
                Text(
                    text = stringResource(Res.string.cart),
                    color = FoodSaverTheme.colorScheme.onBackgroundContrast,
                    style =
                )

                Spacer(Modifier.weight(1f))
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {

            }

            // TODO Delivery info
        }
    }
}