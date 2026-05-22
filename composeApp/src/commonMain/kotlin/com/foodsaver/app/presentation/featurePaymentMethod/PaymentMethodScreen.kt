package com.foodsaver.app.presentation.featurePaymentMethod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodCardModel
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel
import com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod.PaymentMethodAction
import com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod.PaymentMethodEvent
import com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod.PaymentMethodState
import com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod.PaymentMethodViewModel
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featurePaymentMethod.components.NoCardView
import com.foodsaver.app.presentation.featurePaymentMethod.components.PaymentMethodBottomBar
import com.foodsaver.app.presentation.featurePaymentMethod.components.PaymentMethodCard
import com.foodsaver.app.presentation.featurePaymentMethod.components.PaymentMethodTopBar
import com.foodsaver.app.presentation.featurePaymentMethod.components.PaymentMethodTypes
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import com.foodsaver.app.utils.ObserveActions
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun PaymentMethodScreenRoot(
    navController: NavController,
    viewModel: PaymentMethodViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    PaymentMethodScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )

    ObserveActions(viewModel.channel) {
        when (it) {
            is PaymentMethodAction.OnError -> {
                snackbarHostState.showSnackbar(it.message, withDismissAction = true)
            }

            PaymentMethodAction.OnSuccessfulPay -> {
                navController.navigate(Route.PaymentMethodGraph.PaymentSuccessfulScreen) {
                    popUpTo<Route.PaymentMethodGraph.PaymentMethodScreen> {
                        inclusive = true
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodScreen(
    navController: NavController,
    state: PaymentMethodState,
    onEvent: (PaymentMethodEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
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
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
        ) {
            // payment method types
            item {
                Spacer(Modifier.height(30.dp))
                PaymentMethodTypes(
                    types = state.paymentMethodTypes,
                    onTypeClick = { index, type ->
                        onEvent(PaymentMethodEvent.OnChangePaymentMethod(index, type))
                    },
                    isTypeSelected = { index, _ ->
                        index == state.selectedPaymentTypeIndex
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            if (
                state.paymentMethodsByType.isEmpty()
            ) {
                item {
                    Spacer(Modifier.height(25.dp))
                    AnimatedVisibility(
                        visible = state.currentPaymentMethodType != null &&
                                state.currentPaymentMethodType?.name?.startsWith(
                                    "cash",
                                    ignoreCase = true
                                ) == false,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        state.currentPaymentMethodType?.let { type ->
                            NoCardView(
                                type = type,
                                onAddNewClick = {
                                    state.currentPaymentMethodType?.id?.let { id ->
                                        navController.navigate(
                                            Route.PaymentMethodGraph.AddCardScreen(
                                                id
                                            )
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            )
                        }
                    }

                }
            }

            item {
                state.paymentMethodsByType.forEach { card ->
                    Spacer(Modifier.height(25.dp))
                    AnimatedVisibility(
                        visible = state.paymentMethodsByType.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        PaymentMethodCard(
                            card = card,
                            onExpandClick = { /*TODO()*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }
                }
            }
        }
    }
}