package com.foodsaver.app.presentation.featureProfile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.foodsaver.app.presentation.profileAddress.ProfileAddressEvent
import com.foodsaver.app.presentation.profileAddress.ProfileAddressState
import com.foodsaver.app.presentation.profileAddress.ProfileAddressViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileAddressScreenRoot(
    navController: NavController,
    viewModel: ProfileAddressViewModel = koinViewModel()
) {

}

@Composable
private fun ProfileAddressScreen(
    navController: NavController,
    state: ProfileAddressState,
    onEvent: (ProfileAddressEvent) -> Unit
) {

}