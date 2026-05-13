package com.foodsaver.app.presentation.featurePaymentMethod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.featurePaymentMethod.presentation.addCard.AddCardEvent
import com.foodsaver.app.featurePaymentMethod.presentation.addCard.AddCardState
import com.foodsaver.app.featurePaymentMethod.presentation.addCard.AddCardViewModel
import com.foodsaver.app.presentation.featurePaymentMethod.components.AddCardFieldItem
import com.foodsaver.app.presentation.featurePaymentMethod.components.AddCardFieldItemState
import com.foodsaver.app.presentation.featurePaymentMethod.components.AddCardTopBar
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import com.foodsaver.app.utils.date.DateVisualTransformation
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_and_make_payment
import foodsaver.composeapp.generated.resources.card_holder_name
import foodsaver.composeapp.generated.resources.card_number
import foodsaver.composeapp.generated.resources.cvc
import foodsaver.composeapp.generated.resources.expire_date
import foodsaver.composeapp.generated.resources.placeholder_card_holder_name
import foodsaver.composeapp.generated.resources.placeholder_card_number
import foodsaver.composeapp.generated.resources.placeholder_cvc
import foodsaver.composeapp.generated.resources.placeholder_expires_date
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddCardScreenRoot(
    navController: NavController,
    viewModel: AddCardViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    AddCardScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddCardScreenPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(Modifier.padding(padding)) {
                AddCardScreen(
                    navController = rememberNavController(),
                    state = AddCardState(),
                    onEvent = { TODO() }
                )
            }
        }
    }
}

@Composable
private fun AddCardScreen(
    navController: NavController,
    state: AddCardState,
    onEvent: (AddCardEvent) -> Unit
) {

    val firstTextFieldState = listOf(
        AddCardFieldItemState(
            label = Res.string.card_holder_name,
            value = state.cardHolderName,
            onValueChange = { onEvent(AddCardEvent.OnCardHolderNameChange(it)) },
            placeholder = Res.string.placeholder_card_holder_name
        ),
        AddCardFieldItemState(
            label = Res.string.card_number,
            value = state.cardNumber,
            onValueChange = { onEvent(AddCardEvent.OnCardNumberChange(it)) },
            placeholder = Res.string.placeholder_card_number,
            keyboardType = KeyboardType.Number
        ),
    )
    val secondTextFieldState = listOf(
        AddCardFieldItemState(
            label = Res.string.expire_date,
            value = state.expiresDate,
            onValueChange = { onEvent(AddCardEvent.OnExpiresDateChange(it)) },
            placeholder = Res.string.placeholder_expires_date,
            keyboardType = KeyboardType.Number,
            visualTransformation = DateVisualTransformation()
        ),
        AddCardFieldItemState(
            label = Res.string.cvc,
            value = state.cvc,
            onValueChange = { onEvent(AddCardEvent.OnCvcChange(it)) },
            placeholder = Res.string.placeholder_cvc,
            keyboardType = KeyboardType.Number,
            visualTransformation = PasswordVisualTransformation('*')
        ),
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentWindowInsets = WindowInsets.systemBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            AddCardTopBar(
                onCloseIconClick = {
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        },
        bottomBar = {
            val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
            PrimaryButton(
                onClick = {
                    onEvent(AddCardEvent.OnAddCard)
                },
                text = stringResource(Res.string.add_and_make_payment),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(navigationBarPadding)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(25.dp))
            firstTextFieldState.forEach { item ->
                AddCardFieldItem(
                    state = item,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                secondTextFieldState.forEach { item ->
                    AddCardFieldItem(
                        state = item,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}