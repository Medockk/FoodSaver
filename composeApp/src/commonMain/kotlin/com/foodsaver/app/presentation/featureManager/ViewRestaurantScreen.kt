package com.foodsaver.app.presentation.featureManager

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.foodsaver.app.featureRestaurant.viewRestaurant.ViewRestaurantState
import com.foodsaver.app.featureRestaurant.viewRestaurant.ViewRestaurantViewModel
import com.foodsaver.app.presentation.featureHome.components.RestaurantCard
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.restaurants
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ViewMyRestaurantScreenRoot(
    onBackClick: () -> Unit,
    onRestaurantClick: (String) -> Unit,
    viewModel: ViewRestaurantViewModel = koinViewModel(),
    topBar: (@Composable () -> Unit)? = null
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ViewMyRestaurantScreen(
        onRestaurantClick = onRestaurantClick,
        onBackClick = onBackClick,
        state = state,
        topBar = topBar
    )
}

@Composable
private fun ViewMyRestaurantScreen(
    onRestaurantClick: (String) -> Unit,
    onBackClick: () -> Unit,
    state: ViewRestaurantState,
    topBar: (@Composable () -> Unit)?
) {

    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(),
        topBar = {
            if (topBar == null) {
                PrimaryTopBar(
                    title = stringResource(Res.string.restaurants),
                    onNavigationClick = onBackClick,
                )
            } else {
                topBar.invoke()
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(state.restaurants) { restaurant ->
                RestaurantCard(
                    restaurant = restaurant,
                    onRestaurantClick = { onRestaurantClick(restaurant.id) },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
        }
    }
}