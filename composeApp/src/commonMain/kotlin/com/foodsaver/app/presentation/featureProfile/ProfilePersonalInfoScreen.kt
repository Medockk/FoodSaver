package com.foodsaver.app.presentation.featureProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.button.PrimaryTextButton
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureProfile.component.MenuItem
import com.foodsaver.app.presentation.featureProfile.component.MenuItemState
import com.foodsaver.app.presentation.featureProfile.component.ProfileHeader
import com.foodsaver.app.presentation.profilePersonalInfo.ProfilePersonalInfoState
import com.foodsaver.app.presentation.profilePersonalInfo.ProfilePersonalInfoViewModel
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.edit
import foodsaver.composeapp.generated.resources.personal_info
import foodsaver.composeapp.generated.resources.personal_info_icon
import foodsaver.composeapp.generated.resources.profile_email
import foodsaver.composeapp.generated.resources.profile_email_icon
import foodsaver.composeapp.generated.resources.profile_full_name
import foodsaver.composeapp.generated.resources.profile_phone_icon
import foodsaver.composeapp.generated.resources.profile_phone_number
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfilePersonalInfoScreenRoot(
    navController: NavController,
    viewModel: ProfilePersonalInfoViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfilePersonalInfoScreen(
        navController = navController,
        state = state
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfilePersonalInfoScreenPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(Modifier.padding(padding)) {
                ProfilePersonalInfoScreen(
                    navController = rememberNavController(),
                    state = ProfilePersonalInfoState()
                )
            }
        }
    }
}

@Composable
private fun ProfilePersonalInfoScreen(
    navController: NavController,
    state: ProfilePersonalInfoState
) {

    val menuItems = listOf(
        MenuItemState(
            icon = Res.drawable.personal_info_icon,
            title = stringResource(Res.string.profile_full_name),
            onClick = {  },
            subtitle = state.profile?.fullName,
            isClickable = false
        ),
        MenuItemState(
            icon = Res.drawable.profile_email_icon,
            title = stringResource(Res.string.profile_email),
            onClick = {  },
            subtitle = state.profile?.email,
            isClickable = false
        ),
        MenuItemState(
            icon = Res.drawable.profile_phone_icon,
            title = stringResource(Res.string.profile_phone_number),
            onClick = {  },
            subtitle = state.profile?.phone,
            isClickable = false
        ),
    )

    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.personal_info),
                onNavigationClick = {
                    navController.navigateUp()
                },
                actions = {
                    PrimaryTextButton(
                        onClick = {
                            navController.navigate(Route.ProfileGraph.EditProfileScreen)
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.edit).uppercase(),
                            color = FoodSaverTheme.colorScheme.primary,
                            style = FoodSaverTheme.typography.bodySmall,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            state.profile?.let { profile ->
                Spacer(Modifier.height(24.dp))
                ProfileHeader(profile)
            }

            Spacer(Modifier.height(30.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = FoodSaverTheme.colorScheme.backgroundSecondary,
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Spacer(Modifier.height(20.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    menuItems.forEach { item ->
                        MenuItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            state = item,
                            subtitle = item.subtitle ?: ""
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}