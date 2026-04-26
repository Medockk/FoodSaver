package com.foodsaver.app.presentation.featureOnBoarding

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.presentation.featureOnBoarding.components.OnBoardingContent
import com.foodsaver.app.presentation.featureOnBoarding.components.OnBoardingItem
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.on_boarding_icon_1
import foodsaver.composeapp.generated.resources.on_boarding_icon_2
import foodsaver.composeapp.generated.resources.on_boarding_icon_3
import foodsaver.composeapp.generated.resources.on_boarding_icon_4
import foodsaver.composeapp.generated.resources.onboarding_button_end
import foodsaver.composeapp.generated.resources.onboarding_button_skip
import foodsaver.composeapp.generated.resources.onboarding_subtitle_1
import foodsaver.composeapp.generated.resources.onboarding_subtitle_2
import foodsaver.composeapp.generated.resources.onboarding_subtitle_3
import foodsaver.composeapp.generated.resources.onboarding_subtitle_4
import foodsaver.composeapp.generated.resources.onboarding_title_1
import foodsaver.composeapp.generated.resources.onboarding_title_2
import foodsaver.composeapp.generated.resources.onboarding_title_3
import foodsaver.composeapp.generated.resources.onboarding_title_4
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnBoardingScreenRoot(
    navController: NavController,
    onOnboardingComplete: () -> Unit,
) {
    val onBoardingItems = listOf(
        OnBoardingItem(
            image = Res.drawable.on_boarding_icon_1,
            title = Res.string.onboarding_title_1,
            subtitle = Res.string.onboarding_subtitle_1
        ),
        OnBoardingItem(
            image = Res.drawable.on_boarding_icon_2,
            title = Res.string.onboarding_title_2,
            subtitle = Res.string.onboarding_subtitle_2
        ),
        OnBoardingItem(
            image = Res.drawable.on_boarding_icon_3,
            title = Res.string.onboarding_title_3,
            subtitle = Res.string.onboarding_subtitle_3
        ),
        OnBoardingItem(
            image = Res.drawable.on_boarding_icon_4,
            title = Res.string.onboarding_title_4,
            subtitle = Res.string.onboarding_subtitle_4,
            buttonText = Res.string.onboarding_button_end
        ),
    )
    OnBoardingScreen(navController, onBoardingItems, onOnboardingComplete)
}


@Composable
private fun OnBoardingScreen(
    navController: NavController,
    onBoardingItems: List<OnBoardingItem>,
    onOnboardingComplete: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val listSize = remember(onBoardingItems) { onBoardingItems.size }
    val pagerState = rememberPagerState { listSize }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth(),
                pageSpacing = 40.dp,
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) { page ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OnBoardingContent(
                        item = onBoardingItems[page],
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onNext = {
                            if (page + 1 != pagerState.pageCount) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        page = page + 1,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                            } else {
                                onOnboardingComplete()
                            }
                        },
                        itemCount = listSize,
                        currentItem = page
                    )

                    // skip button
                    if (page != (listSize - 1)) {
                        TextButton(
                            onClick = {}
                        ) {
                            Text(
                                text = stringResource(Res.string.onboarding_button_skip),
                                style = FoodSaverTheme.typography.bodyRegular,
                                color = FoodSaverTheme.colorScheme.onBackgroundThin
                            )
                        }
                        Spacer(Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}