package com.foodsaver.app.presentation.featureAdmin

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.featureAdmin.presentation.viewRrestaurant.ViewRestaurantState
import com.foodsaver.app.featureAdmin.presentation.viewRrestaurant.ViewRestaurantViewModel
import com.foodsaver.app.presentation.featureHome.components.RestaurantCard
import com.foodsaver.app.ui.FoodSaverTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ViewRestaurantScreenRoot(
    onBackClick: () -> Unit,
    onUpsertRestaurantClick: (restaurantId: String?) -> Unit,
    viewModel: ViewRestaurantViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ViewRestaurantScreen(
        onBackClick = onBackClick,
        onUpsertRestaurantClick = onUpsertRestaurantClick,
        state = state
    )
}

@Composable
private fun ViewRestaurantScreen(
    onBackClick: () -> Unit,
    onUpsertRestaurantClick: (restaurantId: String?) -> Unit,
    state: ViewRestaurantState,
) {

    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(),
        topBar = {
            PrimaryTopBar(
                title = "Restaurants",
                onNavigationClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(state.restaurants) { restaurant ->
                RestaurantCard(
                    restaurant = restaurant,
                    onRestaurantClick = {
                        onUpsertRestaurantClick(restaurant.id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}