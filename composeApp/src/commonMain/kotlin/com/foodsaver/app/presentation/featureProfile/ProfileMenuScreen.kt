package com.foodsaver.app.presentation.featureProfile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureProfile.component.MenuItemState
import com.foodsaver.app.presentation.featureProfile.component.MenuItems
import com.foodsaver.app.presentation.featureProfile.component.ProfileHeader
import com.foodsaver.app.presentation.profileMenu.ProfileMenuEvent
import com.foodsaver.app.presentation.profileMenu.ProfileMenuState
import com.foodsaver.app.presentation.profileMenu.ProfileMenuViewModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_new_product
import foodsaver.composeapp.generated.resources.addresses_icon
import foodsaver.composeapp.generated.resources.faqs_icon
import foodsaver.composeapp.generated.resources.favorite_icon
import foodsaver.composeapp.generated.resources.logout_icon
import foodsaver.composeapp.generated.resources.more_icon
import foodsaver.composeapp.generated.resources.notifications_icon
import foodsaver.composeapp.generated.resources.payment_method_icon
import foodsaver.composeapp.generated.resources.personal_info
import foodsaver.composeapp.generated.resources.personal_info_icon
import foodsaver.composeapp.generated.resources.profile
import foodsaver.composeapp.generated.resources.profile_menu_addresses
import foodsaver.composeapp.generated.resources.profile_menu_cart
import foodsaver.composeapp.generated.resources.profile_menu_cart_icon
import foodsaver.composeapp.generated.resources.profile_menu_faqs
import foodsaver.composeapp.generated.resources.profile_menu_favorite
import foodsaver.composeapp.generated.resources.profile_menu_logout
import foodsaver.composeapp.generated.resources.profile_menu_notifications
import foodsaver.composeapp.generated.resources.profile_menu_payment_method
import foodsaver.composeapp.generated.resources.profile_menu_settings
import foodsaver.composeapp.generated.resources.profile_menu_user_reviews
import foodsaver.composeapp.generated.resources.settings_icon
import foodsaver.composeapp.generated.resources.user_reviews_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileMenuScreenRoot(
    navController: NavController,
    onBackClick: () -> Unit = { navController.navigateUp() },
    viewModel: ProfileMenuViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileMenuScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
private fun ProfileMenuScreen(
    navController: NavController,
    state: ProfileMenuState,
    onEvent: (ProfileMenuEvent) -> Unit,
    onBackClick: () -> Unit
) {

    val menuItems = listOf(
        if (state.profile?.authorities?.contains("ROLE_MANAGER") == true) {
            listOf(
                MenuItemState(
                    icon = Res.drawable.settings_icon,
                    title = stringResource(Res.string.add_new_product),
                    onClick = { navController.navigate(Route.ManagerGraph.TabsContainer()) }
                )
            )
        }else if (state.profile?.authorities?.contains("ROLE_ADMIN") == true) {
            listOf(
                MenuItemState(
                    icon = Res.drawable.settings_icon,
                    title = "Add restaurant",
                    onClick = { navController.navigate(Route.AdminGraph) }
                )
            )
        }else {
            listOf()
        },
        listOf(
            MenuItemState(
                icon = Res.drawable.personal_info_icon,
                title = stringResource(Res.string.personal_info),
                onClick = { navController.navigate(Route.ProfileGraph.ProfilePersonalInfoScreen) }
            ),
            MenuItemState(
                icon = Res.drawable.addresses_icon,
                title = stringResource(Res.string.profile_menu_addresses),
                onClick = { navController.navigate(Route.ProfileGraph.ProfileAddressScreen) }
            ),
        ),
        listOf(
            MenuItemState(
                icon = Res.drawable.profile_menu_cart_icon,
                title = stringResource(Res.string.profile_menu_cart),
                onClick = { navController.navigate(Route.OrderGraph.OrderScreen) }
            ),
            MenuItemState(
                icon = Res.drawable.favorite_icon,
                title = stringResource(Res.string.profile_menu_favorite),
                onClick = { TODO() }
            ),
            MenuItemState(
                icon = Res.drawable.notifications_icon,
                title = stringResource(Res.string.profile_menu_notifications),
                onClick = { TODO() }
            ),
            MenuItemState(
                icon = Res.drawable.payment_method_icon,
                title = stringResource(Res.string.profile_menu_payment_method),
                onClick = { TODO() }
            ),
        ),
        listOf(
            MenuItemState(
                icon = Res.drawable.faqs_icon,
                title = stringResource(Res.string.profile_menu_faqs),
                onClick = { TODO() }
            ),
            MenuItemState(
                icon = Res.drawable.user_reviews_icon,
                title = stringResource(Res.string.profile_menu_user_reviews),
                onClick = { TODO() }
            ),
            MenuItemState(
                icon = Res.drawable.settings_icon,
                title = stringResource(Res.string.profile_menu_settings),
                onClick = { TODO() }
            ),
        ),
        listOf(
            MenuItemState(
                icon = Res.drawable.logout_icon,
                title = stringResource(Res.string.profile_menu_logout),
                onClick = {
                    onEvent(ProfileMenuEvent.OnLogOutClick)
                }
            ),
        ),
    )

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.profile),
                onNavigationClick = onBackClick,
                actions = {
                    PrimaryFabButton(
                        onClick = {
                            TODO()
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.more_icon),
                            contentDescription = null,
                            tint = FoodSaverTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentPadding = paddingValues
        ) {
            item {
                Spacer(Modifier.height(20.dp))
                state.profile?.let { profile ->
                    ProfileHeader(profile)
                }
            }

            item {
                Spacer(Modifier.height(30.dp))
            }

            items(menuItems) { items ->
                MenuItems(
                    items = items,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}