package com.foodsaver.app.presentation.featureAdmin.route

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.button.PrimaryIconButton
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAdmin.ViewCategoryScreenRoot
import com.foodsaver.app.presentation.featureManager.ViewMyRestaurantScreenRoot
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_icon
import foodsaver.composeapp.generated.resources.category_navigation_icon
import foodsaver.composeapp.generated.resources.restaurant_navigation_icon
import foodsaver.composeapp.generated.resources.restaurants
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

private data class AdminTab(
    val icon: DrawableResource,
    val route: Route,
    val label: String
)

@Composable
internal fun AdminTabsContainer(navController: NavController) {

    val localNavController = rememberNavController()
    val adminTabs = listOf(
        AdminTab(
            Res.drawable.restaurant_navigation_icon,
            Route.AdminGraph.ViewRestaurantScreen,
            "Restaurants"
        ),
        AdminTab(
            Res.drawable.category_navigation_icon,
            Route.AdminGraph.ViewCategoryScreen,
            "Categories"
        ),
    )

    Scaffold(
        contentWindowInsets = WindowInsets(),
        bottomBar = {
            val stackEntry by localNavController.currentBackStackEntryAsState()
            val destination = stackEntry?.destination

            NavigationBar(
                containerColor = FoodSaverTheme.colorScheme.background,
                tonalElevation = 10.dp
            ) {
                adminTabs.forEach { tab ->
                    val isSelected = destination?.hasRoute(tab.route::class) == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            localNavController.navigate(tab.route) {
                                popUpTo(localNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = vectorResource(tab.icon),
                                contentDescription = null,
                                tint = if (isSelected) FoodSaverTheme.colorScheme.primary
                                else FoodSaverTheme.colorScheme.placeholderHint,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(tab.label, color = FoodSaverTheme.colorScheme.onBackground)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = localNavController,
            startDestination = Route.AdminGraph.ViewRestaurantScreen,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Route.AdminGraph.ViewRestaurantScreen> {
                ViewMyRestaurantScreenRoot(
                    onBackClick = { navController.navigateUp() },
                    onRestaurantClick = {
                        navController.navigate(
                            Route.AdminGraph.UpsertRestaurantScreen(
                                it
                            )
                        )
                    },
                    topBar = {
                        PrimaryTopBar(
                            title = stringResource(Res.string.restaurants),
                            onNavigationClick = { navController.navigateUp() },
                            actions = {
                                PrimaryIconButton(onClick = {
                                    navController.navigate(Route.AdminGraph.UpsertRestaurantScreen())
                                }, Res.drawable.add_icon)
                            }
                        )
                    }
                )
            }
            composable<Route.AdminGraph.ViewCategoryScreen> {
                ViewCategoryScreenRoot(
                    onBackClick = { navController.navigateUp() },
                    categoryClick = {
                        navController.navigate(
                            Route.AdminGraph.UpsertCategoryScreen(
                                it
                            )
                        )
                    },
                    onAddCategoryClick = { navController.navigate(Route.AdminGraph.UpsertCategoryScreen()) },
                )
            }
        }
    }
}