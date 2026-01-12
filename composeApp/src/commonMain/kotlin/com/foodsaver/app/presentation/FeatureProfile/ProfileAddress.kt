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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import com.foodsaver.app.common.AuthenticationTextField
import com.foodsaver.app.common.PrimaryButton
import com.foodsaver.app.common.PrimaryCenterAlignedTopAppBar
import com.foodsaver.app.presentation.FeatureProfile.components.AddProfileInfoAlert
import com.foodsaver.app.presentation.FeatureProfile.components.ProfileAddressCard
import com.foodsaver.app.presentation.ProfileAddress.ProfileAddressAction
import com.foodsaver.app.presentation.ProfileAddress.ProfileAddressEvent
import com.foodsaver.app.presentation.ProfileAddress.ProfileAddressState
import com.foodsaver.app.presentation.ProfileAddress.ProfileAddressViewModel
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_new_address
import foodsaver.composeapp.generated.resources.address
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileAddressRoot(
    navController: NavController,
    viewModel: ProfileAddressViewModel = koinViewModel()
) {

    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is ProfileAddressAction.OnError -> {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    ProfileAddress(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )

    if (state.shouldShowDialog) {
        AddProfileInfoAlert(
            content = {
                Column {
                    AuthenticationTextField(
                        value = state.dialogAddressName,
                        onValueChange = {
                            viewModel.onEvent(ProfileAddressEvent.OnDialogAddressNameChange(it))
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = "Address name",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    Spacer(Modifier.height(10.dp))
                    AuthenticationTextField(
                        value = state.dialogAddressValue,
                        onValueChange = {
                            viewModel.onEvent(ProfileAddressEvent.OnDialogAddressValueChange(it))
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = "Address value",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                }
            },
            onSaveButtonClick = {
                viewModel.onEvent(ProfileAddressEvent.OnSaveAddress)
            },
            onDismissRequestClick = {
                viewModel.onEvent(ProfileAddressEvent.OnCloseDialog)
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ProfileAddress(
    navController: NavController,
    state: ProfileAddressState,
    onEvent: (ProfileAddressEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            PrimaryCenterAlignedTopAppBar(
                title = stringResource(Res.string.address),
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
                .padding(horizontal = 20.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                itemsIndexed(state.addresses) { index, address ->
                    ProfileAddressCard(
                        addressModel = address,
                        isSelected = index == 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                    )
                }
            }

            PrimaryButton(
                content = {
                    Text(
                        text = stringResource(Res.string.add_new_address),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onClick = {
                    onEvent(ProfileAddressEvent.OnAddNewAddressClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp)
            )
        }
    }
}