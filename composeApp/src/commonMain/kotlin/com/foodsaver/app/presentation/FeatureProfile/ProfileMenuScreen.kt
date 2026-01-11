@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.FeatureProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.FeatureProfile.components.ProfileInfo
import com.foodsaver.app.presentation.FeatureProfile.components.ProfileMenuItem
import com.foodsaver.app.presentation.ProfileMenu.ProfileAction
import com.foodsaver.app.presentation.ProfileMenu.ProfileEvent
import com.foodsaver.app.presentation.ProfileMenu.ProfileState
import com.foodsaver.app.presentation.ProfileMenu.ProfileViewModel
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.address
import foodsaver.composeapp.generated.resources.ic_back_icon
import foodsaver.composeapp.generated.resources.ic_chat_icon
import foodsaver.composeapp.generated.resources.ic_logout_icon
import foodsaver.composeapp.generated.resources.ic_map_icon
import foodsaver.composeapp.generated.resources.ic_payment_method_icon
import foodsaver.composeapp.generated.resources.ic_personal_info_icon
import foodsaver.composeapp.generated.resources.log_out
import foodsaver.composeapp.generated.resources.menu
import foodsaver.composeapp.generated.resources.payment_method
import foodsaver.composeapp.generated.resources.personal_info
import foodsaver.composeapp.generated.resources.support_service
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileMenuScreenRoot(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel(),
) {

    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is ProfileAction.OnError -> {
                snackbarHostState.showSnackbar(it.message)
            }

            ProfileAction.OnSuccessLogout -> TODO()
        }
    }

    ProfileMenuScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ProfileMenuScreen(
    navController: NavController,
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
) {

    val profileMenuItems = listOf(
        ProfileMenuItem(
            icon = painterResource(Res.drawable.ic_personal_info_icon),
            title = stringResource(Res.string.personal_info),
            onClick = {
                navController.navigate(Route.ProfileGraph.ProfilePersonalInfoScreen)
            }
        ),
        ProfileMenuItem(
            icon = painterResource(Res.drawable.ic_map_icon),
            title = stringResource(Res.string.address),
            onClick = {
                navController.navigate(Route.ProfileGraph.ProfileAddressScreen)
            }
        ),
        ProfileMenuItem(
            icon = painterResource(Res.drawable.ic_payment_method_icon),
            title = stringResource(Res.string.payment_method),
            onClick = {
                navController.navigate(Route.ProfileGraph.ProfilePaymentMethodScreen)
            }
        ),
        ProfileMenuItem(
            icon = painterResource(Res.drawable.ic_chat_icon),
            title = stringResource(Res.string.support_service),
            onClick = {
                navController.navigate(Route.ProfileGraph.ProfileSupportScreen)
            }
        ),
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.menu),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(top = 15.dp)
                .padding(horizontal = 20.dp)
        ) {
            state.profile?.let {
                ProfileInfo(it, modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                profileMenuItems.forEach { profileMenuItem ->
                    ProfileMenuItem(
                        profileMenuItem = profileMenuItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                ProfileMenuItem(
                    profileMenuItem = ProfileMenuItem(
                        icon = painterResource(Res.drawable.ic_logout_icon),
                        title = stringResource(Res.string.log_out),
                        onClick = {
                            onEvent(ProfileEvent.OnLogOutClick)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
        }
    }
}