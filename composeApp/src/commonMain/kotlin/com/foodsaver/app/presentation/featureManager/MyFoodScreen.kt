package com.foodsaver.app.presentation.featureManager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.button.PrimaryTextButton
import com.foodsaver.app.common.product.AddProductCard
import com.foodsaver.app.common.product.ProductCard
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.featureMyFood.presentation.MyFoodEvent
import com.foodsaver.app.featureMyFood.presentation.MyFoodState
import com.foodsaver.app.featureMyFood.presentation.MyFoodViewModel
import com.foodsaver.app.featureSearch.domain.model.ProductCardModel
import com.foodsaver.app.presentation.featureManager.components.MyFoodProductCard
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_icon
import foodsaver.composeapp.generated.resources.add_product
import foodsaver.composeapp.generated.resources.my_food_list
import foodsaver.composeapp.generated.resources.my_food_navigation_icon
import foodsaver.composeapp.generated.resources.restaurant_navigation_icon
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyFoodScreenRoot(
    onBackClick: () -> Unit,
    onProductClick: (String?) -> Unit,
    viewModel: MyFoodViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    MyFoodScreen(
        onBackClick = onBackClick,
        state = state,
        onProductClick = onProductClick
    )
}

@Composable
private fun MyFoodScreen(
    onBackClick: () -> Unit,
    onProductClick: (String?) -> Unit,
    state: MyFoodState
) {

    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.my_food_list),
                onNavigationClick = onBackClick,
                actions = {
                    PrimaryTextButton(
                        onClick = { onProductClick(null) }
                    ) {
                        Text(
                            text = stringResource(Res.string.add_product),
                            color = FoodSaverTheme.colorScheme.primary,
                            style = FoodSaverTheme.typography.bodyMedium
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(state.products) { index, product ->
                val isLeft = index % 2 == 0
                MyFoodProductCard(
                    product = product,
                    onProductClick = { onProductClick(product.productId) },
                    modifier = Modifier
                        .padding(
                            start = if (isLeft) 20.dp else 0.dp,
                            end = if (isLeft) 0.dp else 20.dp
                        )
                )
            }
        }
    }
}