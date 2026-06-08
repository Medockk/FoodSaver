package com.foodsaver.app.presentation.featureOrder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.featureOrder.presentation.OrderEvent
import com.foodsaver.app.featureOrder.presentation.OrderState
import com.foodsaver.app.featureOrder.presentation.OrderViewModel
import com.foodsaver.app.presentation.featureOrder.components.OngoingView
import com.foodsaver.app.presentation.featureOrder.components.OrderTabRow
import com.foodsaver.app.presentation.featureOrder.components.OrderTopBar
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrderScreenRoot(
    navController: NavController,
    viewModel: OrderViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    OrderScreen(navController, state, viewModel::onEvent)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OrderScreenPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                OrderScreen(
                    rememberNavController(),
                    OrderState(),
                    {}
                )
            }
        }
    }
}

@Composable
private fun OrderScreen(
    navController: NavController,
    state: OrderState,
    onEvent: (OrderEvent) -> Unit,
) {

    val pagerState = rememberPagerState { 2 }
    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            OrderTopBar(
                onBackClick = {
                    navController.navigateUp()
                },
                onMoreClick = { onEvent(OrderEvent.OnQrCodeDialogVisibilityChange(true)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OrderTabRow(
                selectedTabIndex = state.tabIndex,
                onTabIndexChange = {
                    onEvent(OrderEvent.OnTabIndexChange(it))
                }
            )

            Spacer(Modifier.height(20.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth(),
                pageSpacing = 48.dp,
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) { page ->
                when (page) {
                    0 -> {
                        OngoingView(state.ongoingOrders, {}, {}, {})
                    }
                    1 -> {

                    }
                }
            }
        }
    }

    AnimatedVisibility(
        state.isQrCodeDialogVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Dialog(
            onDismissRequest = { onEvent(OrderEvent.OnQrCodeDialogVisibilityChange(false)) }
        ) {
            Box(
                modifier = Modifier
                    .background(FoodSaverTheme.colorScheme.background, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                // TODO
                Image(
                    painter = rememberQrCodePainter("foodsaver://app/order"),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(FoodSaverTheme.colorScheme.primary)
                )
            }
        }
    }
}