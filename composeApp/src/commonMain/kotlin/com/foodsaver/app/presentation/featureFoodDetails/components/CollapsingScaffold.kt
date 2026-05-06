package com.foodsaver.app.presentation.featureFoodDetails.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import com.foodsaver.app.ui.FoodSaverTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun CollapsingScaffold(
    image: @Composable () -> Unit,
    toolbarContent: @Composable (progress: Float) -> Unit,
    floatingContent: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    maxHeaderHeight: Dp = 320.dp,
    minHeaderHeight: Dp = 90.dp,
    maxImageWidth: Dp = LocalWindowInfo.current.containerDpSize.width,
    minImageWidth: Dp = 90.dp,
    content: @Composable (PaddingValues) -> Unit
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val maxPx = with(density) { maxHeaderHeight.toPx() }
    val minPx = with(density) { minHeaderHeight.toPx() }
    val range = maxPx - minPx

    val offsetAnim = remember { Animatable(0f) }
    var offsetPx by remember { mutableStateOf(0f) }

    var overscrollPx by remember { mutableStateOf(0f) }

    LaunchedEffect(offsetAnim.value) {
        offsetPx = offsetAnim.value
    }

    val progress = (-offsetPx / range).coerceIn(0f, 1f)
    val easedProgress = FastOutSlowInEasing.transform(progress)

    val nestedScroll = remember {
        object : NestedScrollConnection {

            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y

                if (delta > 0 && offsetPx >= 0f) {
                    overscrollPx += delta * 0.4f * (1 - progress)
                    return Offset.Zero
                }

                val newOffset = (offsetPx + delta).coerceIn(-range, 0f)
                val consumed = newOffset - offsetPx

                offsetPx = newOffset
                coroutineScope.launch {
                    offsetAnim.snapTo(newOffset)
                }

                return Offset(0f, consumed)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // возврат overscroll
                if (overscrollPx > 0) {
                    overscrollPx *= 0.9f
                    if (overscrollPx < 0.5f) overscrollPx = 0f
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity
            ): Velocity {

                val shouldCollapse = when {
                    available.y < -1000f -> true
                    available.y > 1000f -> false
                    else -> offsetPx < -range * 0.3f
                }

                val target = if (shouldCollapse) -range else 0f

                offsetAnim.animateTo(
                    target,
                    animationSpec = spring(
                        dampingRatio = 0.8f,
                        stiffness = 300f
                    )
                )

                overscrollPx = 0f

                return Velocity.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScroll)
    ) {
        val headerHeight =
            maxHeaderHeight +
                    with(density) { offsetPx.toDp() } +
                    with(density) { overscrollPx.toDp() }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .graphicsLayer {
                    translationY = offsetPx * 0.5f
                    alpha = 1f - easedProgress
                }
        ) {
            image()
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
        )

        val imageWidth = lerp(maxImageWidth, minImageWidth, easedProgress)
        val imageHeight = lerp(maxHeaderHeight, minImageWidth, easedProgress)

        val imageTop = lerp(0.dp, 52.dp, easedProgress)
        val imageStart = lerp(0.dp, 24.dp, easedProgress)

        val corner = lerp(0.dp, 100.dp, easedProgress)

        Box(
            modifier = Modifier
                .offset(x = imageStart, y = imageTop)
                .width(imageWidth)
                .height(imageHeight)
                .clip(RoundedCornerShape(corner))
                .graphicsLayer {
                    alpha = easedProgress
                }
                .zIndex(2f)
        ) {
            image()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        0,
                        with(density) { headerHeight.toPx().roundToInt() } - 40
                    )
                }
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
        ) {
            content(PaddingValues(top = 16.dp, bottom = 80.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 24.dp, end = 24.dp)
                .zIndex(3f)
        ) {
            floatingContent()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(minHeaderHeight)
                .zIndex(1f)
                .graphicsLayer {
                    alpha = easedProgress
                }
                .background(
                    Color.White.copy(alpha = 0.7f)
                )
        ) {
            toolbarContent(easedProgress)
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            bottomBar()
        }
    }
}