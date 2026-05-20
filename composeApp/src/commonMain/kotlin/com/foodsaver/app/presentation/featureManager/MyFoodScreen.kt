package com.foodsaver.app.presentation.featureManager

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_icon
import foodsaver.composeapp.generated.resources.my_food_list
import foodsaver.composeapp.generated.resources.my_food_navigation_icon
import foodsaver.composeapp.generated.resources.restaurant_navigation_icon
import org.jetbrains.compose.resources.stringResource

@Composable
fun MyFoodScreenRoot(
    onBackClick: () -> Unit
) {

}

@Composable
private fun MyFoodScreen(
    navController: NavController
) {

    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.my_food_list),
                onNavigationClick = {
                    navController.navigateUp()
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {

        }
    }
}