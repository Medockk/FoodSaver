package com.foodsaver.app.presentation.featureProfile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.coreAddress.domain.model.AddressModel
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureProfile.component.ProfileAddressCard
import com.foodsaver.app.presentation.profileAddress.ProfileAddressEvent
import com.foodsaver.app.presentation.profileAddress.ProfileAddressState
import com.foodsaver.app.presentation.profileAddress.ProfileAddressViewModel
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_new_address
import foodsaver.composeapp.generated.resources.my_address
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileAddressScreenRoot(
    navController: NavController,
    viewModel: ProfileAddressViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileAddressScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileAddressScreenPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                ProfileAddressScreen(
                    navController = rememberNavController(),
                    state = ProfileAddressState(
                        addresses = listOf(
                            AddressModel(
                                id = "TODO()",
                                name = "Home",
                                latitude = 0.0,
                                longitude = 0.0,
                                city = "Orenburg",
                                street = "Chkalova",
                                house = "14",
                                apartment = "4",
                                floor = 1,
                                entrance = null,
                                fullAddress = "город Ореньург, улица Чкалова 14, квартира 4, 1 этаж"
                            ),
                            AddressModel(
                                id = "TODO()",
                                name = "Home",
                                latitude = 0.0,
                                longitude = 0.0,
                                city = "Оренбург",
                                street = "Гагарина",
                                house = "67",
                                apartment = "2",
                                floor = 1,
                                entrance = null,
                                fullAddress = "город Ореньург, улица Гагарина 67, квартира 2, 1 этаж"
                            ),
                        )
                    ),
                    onEvent = { /*TODO()*/ }
                )
            }
        }
    }
}

@Composable
private fun ProfileAddressScreen(
    navController: NavController,
    state: ProfileAddressState,
    onEvent: (ProfileAddressEvent) -> Unit
) {
    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.my_address),
                onNavigationClick = {
                    navController.navigateUp()
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                contentPadding = PaddingValues(top = 24.dp)
            ) {
                items(state.addresses) { address ->
                    ProfileAddressCard(
                        address = address,
                        modifier = Modifier
                            .fillMaxWidth(),
                        onEditAddressClick = {},
                        onDeleteAddressClick = {
                            onEvent(ProfileAddressEvent.OnDeleteAddress(address))
                        }
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }

            PrimaryButton(
                onClick = {
                    navController.navigate(Route.ProfileGraph.AddNewAddressScreen)
                },
                text = stringResource(Res.string.add_new_address).uppercase(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}