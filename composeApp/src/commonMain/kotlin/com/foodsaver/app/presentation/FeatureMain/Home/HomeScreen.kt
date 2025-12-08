@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.FeatureMain.Home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.common.SearchTextField
import com.foodsaver.app.presentation.FeatureMain.Home.components.CategoryChip
import com.foodsaver.app.presentation.FeatureMain.Home.components.ProductCard
import com.foodsaver.app.presentation.Home.HomeAction
import com.foodsaver.app.presentation.Home.HomeEvent
import com.foodsaver.app.presentation.Home.HomeState
import com.foodsaver.app.presentation.Home.HomeViewModel
import com.foodsaver.app.presentation.MainRoute
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_burger_icon
import foodsaver.composeapp.generated.resources.ic_location_icon
import foodsaver.composeapp.generated.resources.search
import foodsaver.composeapp.generated.resources.search_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SharedTransitionScope.HomeScreenRoot(
    navController: NavController,
    animatedContentScope: AnimatedContentScope,
    viewModel: HomeViewModel = koinViewModel()
) {

    val state = viewModel.state.value
    val channel = viewModel.channel
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveActions(channel) {
        when (it) {
            is HomeAction.OnError -> {
                snackBarHostState.showSnackbar(it.message)
            }
        }
    }

    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackBarHostState,
        animatedContentScope = animatedContentScope,
        navController = navController
    )
}

@Composable
private fun SharedTransitionScope.HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    animatedContentScope: AnimatedContentScope,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val lazyColumnState = rememberLazyListState()
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Moscow",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_burger_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(15.dp),
                                tint = Color.Unspecified
                            )
                        }
                        Spacer(Modifier.width(30.dp))
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_location_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(16.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            item {
                Spacer(Modifier.height(20.dp))
                SearchTextField(
                    value = state.searchQuery,
                    onValueChange = { onEvent(HomeEvent.OnSearchQueryChange(it)) },
                    onSearch = {  },
                    modifier = Modifier
                        .fillMaxWidth(),
                    hint = stringResource(Res.string.search),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.search_icon),
                            contentDescription = stringResource(Res.string.search),
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                )

                Spacer(Modifier.height(20.dp))
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),

                ) {
                    itemsIndexed(
                        items = state.categories,
                        key = { index, _ ->
                            index
                        }
                    ) { index, category ->
                        CategoryChip(
                            label = category.categoryName,
                            isSelected = index == state.categoryIndex,
                            onClick = {
                                onEvent(HomeEvent.OnCategoryIndexChange(index))
                            }
                        )
                    }
                }
                Spacer(Modifier.height(28.dp))
            }

            if (state.categoryIndex == null) {
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier
                            .fillParentMaxSize()
                    ) {
                        this@LazyVerticalGrid.items(
                            items = state.products,
                            key = { it.productId }
                        ) {product ->
                            val isInCart = remember { mutableStateOf(false) }
                            with(animatedContentScope) {
                                ProductCard(
                                    product = product,
                                    isInCart = isInCart.value,
                                    onProductClick = { productId ->
                                        navController.navigate(route = MainRoute.ProductScreen(productId))
                                    },
                                    onAddProductClick = { isInCart.value = !isInCart.value },
                                    modifier = Modifier
                                        .fillParentMaxWidth(0.5f)
                                )
                            }
                        }
                    }
                }
            } else {
                items(
                    items = state.productsByCategory,
                    key = { it.productId }
                ) {product ->
                    with(animatedContentScope) {
                        ProductCard(
                            product = product,
                            isInCart = false,
                            onProductClick = {  },
                            onAddProductClick = {  },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                        )
                    }
                }
            }
        }
    }
}