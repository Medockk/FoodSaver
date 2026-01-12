package com.foodsaver.app.presentation.FeatureProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.foodsaver.app.common.PrimaryButton
import com.foodsaver.app.common.PrimaryCenterAlignedTopAppBar
import com.foodsaver.app.presentation.FeatureProfile.components.ProfilePaymentCard
import com.foodsaver.app.presentation.ProfilePaymentMethod.ProfilePaymentMethodAction
import com.foodsaver.app.presentation.ProfilePaymentMethod.ProfilePaymentMethodEvent
import com.foodsaver.app.presentation.ProfilePaymentMethod.ProfilePaymentMethodState
import com.foodsaver.app.presentation.ProfilePaymentMethod.ProfilePaymentMethodViewModel
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_new_card
import foodsaver.composeapp.generated.resources.payment_method
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfilePaymentMethodScreenRoot(
    navController: NavController,
    viewModel: ProfilePaymentMethodViewModel = koinViewModel(),
) {

    val snackbarHostState = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is ProfilePaymentMethodAction.OnError -> {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    ProfilePaymentMethodScreen(
        navController = navController,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun ProfilePaymentMethodScreen(
    navController: NavController,
    state: ProfilePaymentMethodState,
    onEvent: (ProfilePaymentMethodEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        topBar = {
            PrimaryCenterAlignedTopAppBar(
                title = stringResource(Res.string.payment_method),
                onNavigationButtonClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 20.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                itemsIndexed(state.cards) { index, card ->
                    ProfilePaymentCard(
                        paymentCardModel = card,
                        isSelected = index == 0,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            PrimaryButton(
                content = {
                    Text(
                        text = stringResource(Res.string.add_new_card),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                modifier = Modifier
                    .heightIn(min = 60.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = {
                    onEvent(ProfilePaymentMethodEvent.OnAddNewCardClick)
                }
            )
        }
    }
}