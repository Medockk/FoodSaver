@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class,
    FlowPreview::class
)

package com.foodsaver.app.presentation.featureHome

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.PrimaryPullToRefreshBox
import com.foodsaver.app.common.SearchTextField
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.Home.HomeAction
import com.foodsaver.app.presentation.Home.HomeEvent
import com.foodsaver.app.presentation.Home.HomeState
import com.foodsaver.app.presentation.Home.HomeViewModel
import com.foodsaver.app.presentation.Home.ProductsDisplayMode
import com.foodsaver.app.presentation.featureHome.components.CategoryHeader
import com.foodsaver.app.presentation.featureHome.components.HomeModalDrawerSheet
import com.foodsaver.app.presentation.featureHome.components.HomeTopAppBar
import com.foodsaver.app.presentation.featureHome.components.OfferHeader
import com.foodsaver.app.presentation.featureHome.components.ProductCard
import com.foodsaver.app.presentation.featureHome.components.ShimmerProductCard
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.search
import foodsaver.composeapp.generated.resources.search_icon
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SharedTransitionScope.HomeScreenRoot(
    navController: NavController,
    animatedContentScope: AnimatedContentScope,
    viewModel: HomeViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val channel = viewModel.channel
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveActions(channel) {
        when (it) {
            is HomeAction.OnError -> {
                val currentMessage = snackBarHostState.currentSnackbarData
                    ?.visuals?.message

                if (currentMessage != it.message) {
                    snackBarHostState.showSnackbar(
                        message = it.message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is HomeAction.OnProductNavigation -> {
                navController.navigate(
                    Route.MainGraph.ProductDetailScreen(
                        it.productId,
                        it.isProductInCart,
                        it.cartProductCount ?: 1L
                    )
                )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    SharedTransitionLayout {
        AnimatedContent(true) {
            HomeScreen(
                state = HomeState(),
                onEvent = { },
                snackbarHostState = SnackbarHostState(),
                animatedContentScope = this,
                navController = rememberNavController(),
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun SharedTransitionScope.HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    animatedContentScope: AnimatedContentScope,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    val lazyGridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val modalDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val shouldStartPaginate = retain {
        derivedStateOf {
            val layoutInfo = lazyGridState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)

            totalItemsNumber > 0 && lastVisibleItemIndex >= (totalItemsNumber - 2)
        }
    }
    LaunchedEffect(shouldStartPaginate.value) {
        if (shouldStartPaginate.value && !state.isProductsLoading) {
            onEvent(HomeEvent.LoadNextProducts)
        }
    }

    val offerPagerState = rememberPagerState {
        if (state.offers.isEmpty() && state.isOffersLoading) 3
        else state.offers.size
    }

    var isSearchFieldTriggered by retain {
        mutableStateOf(state.searchQuery.isNotBlank())
    }

    ModalNavigationDrawer(
        drawerState = modalDrawerState,
        drawerContent = {
            HomeModalDrawerSheet(
                navController = navController,
                modalDrawerState = modalDrawerState,
                roles = state.profile?.roles ?: emptyList()
            )
        },
        scrimColor = FoodSaverTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            modifier = modifier
                .fillMaxSize()
                .imePadding(),
            containerColor = FoodSaverTheme.colorScheme.background,
            topBar = {
                HomeTopAppBar(
                    currentAddress = state.currentAddress,
                    cartProductQuantity = state.cartProducts.size,
                    onBurgerClick = {
                        coroutineScope.launch {
                            modalDrawerState.open()
                        }
                    },
                    onCartClick = { navController.navigate(Route.MainGraph.CartScreen) }
                )
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = lazyGridState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item(
                    span = { GridItemSpan(maxLineSpan) },
                    key = "StickyHeader",
                    contentType = "StickyHeader"
                ) {
                    Column {
                        OfferHeader(
                            offers = state.offers,
                            offerPagerState = offerPagerState,
                            isOffersLoading = state.isOffersLoading,
                            onOfferClick = { onEvent(HomeEvent.OnOfferClick(it)) }
                        )
                        Spacer(Modifier.height(20.dp))
                        SearchTextField(
                            value = state.searchQuery,
                            onValueChange = { onEvent(HomeEvent.OnSearchQueryChange(it)) },
                            onSearch = { },
                            modifier = Modifier
                                .fillMaxWidth(),
                            hint = stringResource(Res.string.search),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(Res.drawable.search_icon),
                                    contentDescription = stringResource(Res.string.search),
                                    tint = FoodSaverTheme.colorScheme.onSecondary,
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }
                        )
                        Spacer(Modifier.height(20.dp))

                        CategoryHeader(
                            categories = state.categories,
                            isCategoriesLoading = state.isCategoriesLoading,
                            selectedCategoryIds = state.selectedCategoryIds,
                            onCategoryClick = { onEvent(HomeEvent.OnCategoryIndexChange(it)) }
                        )
                    }
                }

                if (state.isProductsLoading && (state.products.isEmpty()/* || state.searchedProducts.isEmpty()*/)) {
                    items(6) {
                        ShimmerProductCard(Modifier.fillMaxWidth(0.5f))
                    }
                } else {
                    items(
                        items = when (state.productsDisplayMode) {
                            ProductsDisplayMode.All -> state.products
                            ProductsDisplayMode.Searched -> state.searchedProducts
                        },
                        key = { it.productId },
                        contentType = { "Products" }
                    ) { product ->
                        with(animatedContentScope) {
                            val isInCart = state.cartProductIds.contains(product.productId)
                            ProductCard(
                                product = product,
                                isInCart = isInCart,
                                onProductClick = { productId: String ->
                                    onEvent(HomeEvent.OnProductClick(productId))
                                },
                                onAddProductClick = {
                                    onEvent(HomeEvent.OnAddProductToCart(it))
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
//                                    .animateItem()
                            )
                        }
                    }
                }

                if (state.isProductsLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        AnimatedVisibility(
                            visible = state.isProductsLoading,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 40.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}