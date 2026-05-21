package com.foodsaver.app.presentation.featureManager

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.product.AddProductCard
import com.foodsaver.app.common.product.ProductCard
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.featureMyFood.presentation.MyFoodEvent
import com.foodsaver.app.featureMyFood.presentation.MyFoodState
import com.foodsaver.app.featureMyFood.presentation.MyFoodViewModel
import com.foodsaver.app.featureSearch.domain.model.ProductCardModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_icon
import foodsaver.composeapp.generated.resources.my_food_list
import foodsaver.composeapp.generated.resources.my_food_navigation_icon
import foodsaver.composeapp.generated.resources.restaurant_navigation_icon
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyFoodScreenRoot(
    onBackClick: () -> Unit,
    viewModel: MyFoodViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    MyFoodScreen(
        onBackClick = onBackClick,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun MyFoodScreen(
    onBackClick: () -> Unit,
    state: MyFoodState,
    onEvent: (MyFoodEvent) -> Unit
) {

    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.my_food_list),
                onNavigationClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues
        ) {
            itemsIndexed(state.products) { index, product ->
                Text(text = product.name)
            }
        }
    }
}