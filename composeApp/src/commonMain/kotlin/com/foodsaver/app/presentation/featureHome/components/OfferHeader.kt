package com.foodsaver.app.presentation.featureHome.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodsaver.app.domain.model.OfferModel
import com.foodsaver.app.ui.FoodSaverTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun OfferHeader(
    offers: List<OfferModel>,
    offerPagerState: PagerState,
    isOffersLoading: Boolean,
    onOfferClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (offers.isNotEmpty()) {
            LaunchedEffect(Unit) {
                withContext(Dispatchers.Default) {
                    while (true) {
                        delay(5000L)
                        if (!offerPagerState.isScrollInProgress) {
                            val nextPage =
                                (offerPagerState.currentPage + 1) % offerPagerState.pageCount

                            withContext(Dispatchers.Main) {
                                offerPagerState.animateScrollToPage(
                                    nextPage,
                                    animationSpec = tween(650)
                                )
                            }
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
            ) { page ->
                val isLoading = remember(isOffersLoading, offers) {
                    isOffersLoading && offers.isEmpty()
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
                    remember(offers) {
                        offers.getOrNull(page)
                    }?.let {
                        OfferCard(
                            offer = it,
                            onClick = { id: String ->
                                onOfferClick(id)
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

}