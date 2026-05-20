package com.foodsaver.app.presentation.featureAdmin

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.featureCategory.presentation.viewCategory.ViewCategoryState
import com.foodsaver.app.featureCategory.presentation.viewCategory.ViewCategoryViewModel
import com.foodsaver.app.presentation.featureAdmin.components.CategoryCart
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_icon
import foodsaver.composeapp.generated.resources.categories
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ViewCategoryScreenRoot(
    onBackClick: () -> Unit,
    categoryClick: (categoryId: String) -> Unit,
    onAddCategoryClick:() -> Unit,
    viewModel: ViewCategoryViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ViewCategoryScreen(
        onBackClick = onBackClick,
        categoryClick = categoryClick,
        onAddCategoryClick = onAddCategoryClick,
        onRefresh = viewModel::onRefresh,
        state = state
    )
}

@Composable
private fun ViewCategoryScreen(
    onBackClick: () -> Unit,
    categoryClick: (categoryId: String) -> Unit,
    onAddCategoryClick:() -> Unit,
    onRefresh: () -> Unit,
    state: ViewCategoryState
) {

    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.categories),
                onNavigationClick = onBackClick,
                actions = {
                    IconButton(
                        onClick = onAddCategoryClick
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.add_icon),
                            contentDescription = null,
                            tint = FoodSaverTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = state.isRefreshing,
                    modifier = Modifier.padding(paddingValues).align(Alignment.TopCenter)
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = paddingValues
            ) {
                items(state.allCategories) { category ->
                    CategoryCart(
                        category = category,
                        onCategoryClick = { categoryClick(category.categoryId) },
                        onDeleteClick = { categoryClick(category.categoryId) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp).padding(horizontal = 24.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}