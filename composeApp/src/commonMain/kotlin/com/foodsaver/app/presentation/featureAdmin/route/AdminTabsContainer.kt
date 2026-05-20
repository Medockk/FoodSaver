package com.foodsaver.app.presentation.featureAdmin.route

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAdmin.ViewCategoryScreen
import com.foodsaver.app.presentation.featureAdmin.ViewRestaurantScreenRoot
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.category_navigation_icon
import foodsaver.composeapp.generated.resources.restaurant_navigation_icon
import org.jetbrains.compose.resources.DrawableResource
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
        AdminTab(Res.drawable.restaurant_navigation_icon, Route.AdminGraph.ViewRestaurantScreen, "Restaurants"),
        AdminTab(Res.drawable.category_navigation_icon, Route.AdminGraph.ViewRestaurantScreen, "Categories"),
    )

    Scaffold(
        bottomBar = {
            val stackEntry by localNavController.currentBackStackEntryAsState()
            val destination = stackEntry?.destination

            NavigationBar {
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
                                else FoodSaverTheme.colorScheme.placeholderHint
                            )
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
                ViewRestaurantScreenRoot(
                    onBackClick = { navController.navigateUp() },
                    onUpsertRestaurantClick = { navController.navigate(Route.AdminGraph.UpsertRestaurantScreen(it)) }
                )
            }
            composable<Route.AdminGraph.ViewCategoryScreen> {
                ViewCategoryScreen(
                    onBackClick = { navController.navigateUp() },
                    onUpsertCategoryClick = { navController.navigate(Route.AdminGraph.UpsertCategoryScreen(it)) }
                )
            }
        }
    }
}