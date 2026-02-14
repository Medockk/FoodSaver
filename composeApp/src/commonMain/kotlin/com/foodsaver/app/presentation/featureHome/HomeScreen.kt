@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class,
    FlowPreview::class
)

package com.foodsaver.app.presentation.featureHome

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.PrimaryPullToRefreshBox
import com.foodsaver.app.common.SearchTextField
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.Home.HomeAction
import com.foodsaver.app.presentation.Home.HomeEvent
import com.foodsaver.app.presentation.Home.HomeState
import com.foodsaver.app.presentation.Home.HomeViewModel
import com.foodsaver.app.presentation.Home.ProductsDisplayMode
import com.foodsaver.app.presentation.featureHome.components.CategoryChip
import com.foodsaver.app.presentation.featureHome.components.HomeModalDrawerSheet
import com.foodsaver.app.presentation.featureHome.components.HomeTopAppBar
import com.foodsaver.app.presentation.featureHome.components.OfferCard
import com.foodsaver.app.presentation.featureHome.components.ProductCard
import com.foodsaver.app.presentation.featureHome.components.ShimmerOfferCard
import com.foodsaver.app.presentation.featureHome.components.ShimmerProductCard
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.search
import foodsaver.composeapp.generated.resources.search_icon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    var shouldLoadNextPage by remember { mutableStateOf(false) }
    val isProductsLoading by rememberUpdatedState(state)
    val coroutineScope = rememberCoroutineScope()
    val modalDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            snapshotFlow {
                val lastItem = lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()
                val totalItems = lazyGridState.layoutInfo.totalItemsCount

                val result = (lastItem?.index ?: 0) >= totalItems - 1
                println("SNAPSHOT $totalItems")
                result
            }
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect { isEnd ->
                    if (isEnd && !isProductsLoading.isProductsLoading) {
                        onEvent(HomeEvent.LoadNextProducts)
                    }
                    shouldLoadNextPage = isEnd
                }
        }
    }

    val offerPagerState = rememberPagerState {
        if (state.offers.isEmpty() && state.isOffersLoading) 3
        else state.offers.size
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
                item(span = { GridItemSpan(maxLineSpan) }) {

                    if (state.offers.isNotEmpty()) {
                        LaunchedEffect(Unit) {
                            withContext(Dispatchers.Default) {
                                while (true) {
                                    delay(5000L)
                                    if (!offerPagerState.isScrollInProgress) {
                                        val nextPage =
                                            (offerPagerState.currentPage + 1) % offerPagerState.pageCount
                                        offerPagerState.animateScrollToPage(
                                            nextPage,
                                            animationSpec = tween(650)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HorizontalPager(
                            state = offerPagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            pageSpacing = 20.dp,
                        )
                        { page ->
                            val isLoading = remember(state.isOffersLoading, state.offers) {
                                state.isOffersLoading && state.offers.isEmpty()
                            }
                            AnimatedVisibility(
                                visible = isLoading,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                ShimmerOfferCard()
                            }
                            AnimatedVisibility(
                                visible = !isLoading,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                remember(state.offers) {
                                    state.offers.getOrNull(page)
                                }?.let {
                                    OfferCard(
                                        offer = it,
                                        onClick = { id: String ->
                                            onEvent(HomeEvent.OnOfferClick(id))
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val primarySelectedColor = FoodSaverTheme.colorScheme.primary
                            val primaryUnselectedColor = Color(0xFFE2E2E2)
                            repeat(offerPagerState.pageCount) { index ->
                                Box(
                                    Modifier
                                        .size(9.dp)
                                        .clip(CircleShape)
                                        .drawWithCache {
                                            onDrawWithContent {
                                                val isSelected =
                                                    offerPagerState.currentPage == index
                                                drawCircle(if (isSelected) primarySelectedColor else primaryUnselectedColor)
                                            }
                                        }
                                )
                                Spacer(Modifier.width(4.dp))
                            }
                        }
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
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
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Crossfade(
                        targetState = state.isCategoriesLoading && state.categories.isEmpty()
                    ) { isLoading ->
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            if (isLoading) {
                                items(6) {
                                    Box(
                                        Modifier.size(70.dp, 30.dp).clip(RoundedCornerShape(20.dp))
                                            .shimmerEffect()
                                    )
                                }
                            } else {
                                items(
                                    items = state.categories,
                                    key = { category -> category.categoryId }
                                ) { category ->
                                    val isSelected =
                                        state.selectedCategoryIds.contains(category.categoryId)
                                    CategoryChip(
                                        label = category.categoryName,
                                        isSelected = isSelected,
                                        onClick = {
                                            onEvent(HomeEvent.OnCategoryIndexChange(category.categoryId))
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(28.dp))
                }

                if (state.isProductsLoading && (state.products.isEmpty() || state.searchedProducts.isEmpty())) {
                    items(6) {
                        ShimmerProductCard(Modifier.fillMaxWidth(0.5f).animateItem())
                    }
                } else {
                    items(
                        items = when (state.productsDisplayMode) {
                            ProductsDisplayMode.All -> state.products
                            ProductsDisplayMode.Searched -> state.searchedProducts
                        },
                        key = { it.productId }
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
                                    .animateItem()
                            )
                        }
                    }
                }

                if (shouldLoadNextPage && state.isProductsLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        AnimatedVisibility(
                            visible = shouldLoadNextPage && state.isProductsLoading,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier
                                .animateItem()
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