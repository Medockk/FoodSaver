package com.foodsaver.app.presentation.featureManager.route

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureManager.MyFoodScreenRoot
import com.foodsaver.app.presentation.featureManager.ViewMyRestaurantScreenRoot
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_icon
import foodsaver.composeapp.generated.resources.my_food_navigation_icon
import foodsaver.composeapp.generated.resources.restaurant_navigation_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

data class Tabs(
    val icon: DrawableResource,
    val route: Route
)

@Composable
fun ManagerTabsContainer(
    rootNavController: NavController,
    targetTabs: Route.ManagerGraph.TabsContainer.Tabs
) {

    val localController = rememberNavController()
    val tabs = listOf(
        Tabs(Res.drawable.my_food_navigation_icon, Route.ManagerGraph.MyFoodScreen),
        Tabs(Res.drawable.add_icon, Route.ManagerGraph.AddProductScreen()),
        Tabs(Res.drawable.restaurant_navigation_icon, Route.ManagerGraph.ViewMyRestaurantScreen),
    )

    val computedTargetTab = when (targetTabs) {
        Route.ManagerGraph.TabsContainer.Tabs.MyFood -> Route.ManagerGraph.MyFoodScreen
    }

    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .dropShadow(
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        shadow = Shadow(
                            radius = 12.dp,
                            color = FoodSaverTheme.colorScheme.primaryShadowColor,
                            alpha = .15f,
                            offset = DpOffset(x = 12.dp, 12.dp)
                        )
                    ).clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(FoodSaverTheme.colorScheme.background)
            ) {

                val navBackStackEntry by localController.currentBackStackEntryAsState()
                val destination = navBackStackEntry?.destination

                tabs.forEach { tab ->
                    val isSelected = destination?.hasRoute(tab.route::class) == true
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (tab.route is Route.ManagerGraph.AddProductScreen) {
                                rootNavController.navigate(Route.ManagerGraph.AddProductScreen())
                            } else {
                                localController.navigate(tab.route) {
                                    popUpTo(localController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            if (tab.route is Route.ManagerGraph.AddProductScreen) {
                                Box(
                                    modifier = Modifier
                                        .size(57.dp)
                                        .clip(CircleShape)
                                        .background(FoodSaverTheme.colorScheme.primaryThin)
                                        .border(
                                            width = 1.dp,
                                            color = FoodSaverTheme.colorScheme.primary,
                                            shape = CircleShape
                                        )
                                )
                            } else {
                                Icon(
                                    imageVector = vectorResource(tab.icon),
                                    contentDescription = null,
                                    tint = if (isSelected) FoodSaverTheme.colorScheme.primary
                                    else FoodSaverTheme.colorScheme.placeholderHint,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = localController,
            startDestination = computedTargetTab,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Route.ManagerGraph.ViewMyRestaurantScreen> {
                ViewMyRestaurantScreenRoot(
                    onBackClick = { rootNavController.navigateUp() },
                    onRestaurantClick = { rootNavController.navigate(Route.UpsertRestaurantGraph.UpsertRestaurantScreen(it)) }
                )
            }
            composable<Route.ManagerGraph.MyFoodScreen> {
                MyFoodScreenRoot(
                    onBackClick = {
                        rootNavController.navigateUp()
                    }
                )
            }

        }
    }
}