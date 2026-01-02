@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.FeatureHome

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.foodsaver.app.common.PrimaryPullToRefreshBox
import com.foodsaver.app.common.SearchTextField
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.presentation.FeatureHome.components.CategoryChip
import com.foodsaver.app.presentation.FeatureHome.components.ProductCard
import com.foodsaver.app.presentation.Home.HomeAction
import com.foodsaver.app.presentation.Home.HomeEvent
import com.foodsaver.app.presentation.Home.HomeState
import com.foodsaver.app.presentation.Home.HomeViewModel
import com.foodsaver.app.presentation.routing.Route
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_burger_icon
import foodsaver.composeapp.generated.resources.ic_cart_icon
import foodsaver.composeapp.generated.resources.ic_location_icon
import foodsaver.composeapp.generated.resources.poppins_black
import foodsaver.composeapp.generated.resources.search
import foodsaver.composeapp.generated.resources.search_icon
import org.jetbrains.compose.resources.Font
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

    PrimaryPullToRefreshBox(
        isRefreshing = state.isRefresh,
        onRefresh = viewModel::onRefresh
    ) {
        HomeScreen(
            state = state,
            onEvent = viewModel::onEvent,
            snackbarHostState = snackBarHostState,
            animatedContentScope = animatedContentScope,
            navController = navController
        )
    }
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
                    if (state.profile != null) {
                        state.profile?.let { profile ->
                            Text(
                                text = profile.currentCity ?: "",
                                color = MaterialTheme.colorScheme.secondaryFixedDim,
                                fontSize = 12.sp
                            )
                        }
                    } else {
                        Box(Modifier.size(100.dp, 15.dp).shimmerEffect())
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(Route.ProfileGraph)
                            }
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
                },
                actions = {
                    BadgedBox(
                        badge = {
                            Badge(
                                modifier = Modifier
                                    .size(15.dp)
                                    .align(Alignment.TopEnd)
                                    .background(MaterialTheme.colorScheme.background, CircleShape)
                                    .padding(0.5.dp)
                                    .background(MaterialTheme.colorScheme.error, CircleShape),
                                containerColor = Color.Unspecified
                            ) {
                                if (state.cartProducts.isNotEmpty()) {
                                    Text(
                                        text = state.cartProducts.size.toString(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily(Font(Res.font.poppins_black))
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(Route.MainGraph.CartScreen)
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_cart_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(24.dp)
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
                Column() {
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
                                if (index == state.categoryIndex) {
                                    onEvent(HomeEvent.OnCategoryIndexChange(null))
                                } else {
                                    onEvent(HomeEvent.OnCategoryIndexChange(index))
                                }
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
                        ) { product ->
                            with(animatedContentScope) {
                                val isInCart = state.cartProductIds.contains(product.productId)
                                ProductCard(
                                    product = product,
                                    isInCart = isInCart,
                                    onProductClick = { productId ->
                                        navController.navigate(route = Route.MainGraph.ProductDetailScreen(
                                            productId = productId,
                                            isProductInCart = isInCart
                                        ))
                                    },
                                    onAddProductClick = {
                                        onEvent(HomeEvent.OnAddProductToCart(it))
                                    },
                                    modifier = Modifier
                                        .fillParentMaxWidth(0.5f)
                                )
                            }
                        }
                    }
                }
            }
            else {
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