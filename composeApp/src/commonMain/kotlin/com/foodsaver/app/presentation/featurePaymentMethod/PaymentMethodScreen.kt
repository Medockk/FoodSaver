package com.foodsaver.app.presentation.featurePaymentMethod

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod.PaymentMethodEvent
import com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod.PaymentMethodState
import com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod.PaymentMethodViewModel
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featurePaymentMethod.components.PaymentMethodBottomBar
import com.foodsaver.app.presentation.featurePaymentMethod.components.PaymentMethodTopBar
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PaymentMethodScreenRoot(
    navController: NavController,
    viewModel: PaymentMethodViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PaymentMethodScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PaymentMethodScreenPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                PaymentMethodScreen(
                    navController = rememberNavController(),
                    state = PaymentMethodState(),
                    onEvent = { TODO() }
                )
            }
        }
    }
}

@Composable
private fun PaymentMethodScreen(
    navController: NavController,
    state: PaymentMethodState,
    onEvent: (PaymentMethodEvent) -> Unit
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            PaymentMethodTopBar(
                onBackClick = {
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        },
        bottomBar = {
            PaymentMethodBottomBar(
                totalPrice = state.totalPrice,
                currency = state.currency,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                onPayClick = {
                    onEvent(PaymentMethodEvent.OnPayClick)
                }
            )
        }
    ) { padding ->

    }
}