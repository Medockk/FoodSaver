package com.foodsaver.app.common.collapsingToolbar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun CollapsingToolbarImage(
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    centralContent: (@Composable () -> Unit)? = null,
    additionalContent: (@Composable () -> Unit)? = null,
    collapsingImage: (@Composable (progress: Float) -> Unit)? = null,
    scrollBehavior: CustomToolbarScrollBehavior? = null,
    collapsedElevation: Dp = DefaultCollapsedElevation,
    expandedImageHeight: Dp = 240.dp,
    collapsedImageSize: Dp = 45.dp
) {
    val collapsedFraction = when {
        scrollBehavior != null && centralContent == null -> scrollBehavior.state.collapsedFraction
        scrollBehavior != null && centralContent != null -> 0f
        else -> 1f
    }

    val elevationState =
        animateDpAsState(if (collapsedFraction > 0.9f) collapsedElevation else 0.dp)

    Surface(
        modifier = modifier,
        shadowElevation = elevationState.value,
    ) {
        Layout(
            content = {
                if (collapsingImage != null) {
                    Box(modifier = Modifier.layoutId(ExpandedTitleId)) { collapsingImage(collapsedFraction) }
                }
                if (navigationIcon != null) {
                    Box(modifier = Modifier.layoutId(NavigationIconId)) { navigationIcon() }
                }
                if (actions != null) {
                    Row(modifier = Modifier.layoutId(ActionsId)) { actions() }
                }
                if (additionalContent != null) {
                    Box(modifier = Modifier.fillMaxWidth().layoutId(AdditionalContentId)) {
                        additionalContent()
                    }
                }
            },
            modifier = modifier.then(Modifier.heightIn(min = MinCollapsedHeight))
        ) { measurables, constraints ->

            val collapsedSizePx = collapsedImageSize.toPx()
            val expandedHeightPx = expandedImageHeight.toPx()
            val navigationIconPlaceable =
                measurables.firstOrNull { it.layoutId == NavigationIconId }
                    ?.measure(constraints.copy(minWidth = 0))
            val actionsPlaceable = measurables.firstOrNull { it.layoutId == ActionsId }
                ?.measure(constraints.copy(minWidth = 0))
            val additionalContentPlaceable =
                measurables.firstOrNull { it.layoutId == AdditionalContentId }?.measure(constraints)

            val navWidth = navigationIconPlaceable?.width ?: 0
            val actionsWidth = actionsPlaceable?.width ?: 0

            // 1. ВЫЧИСЛЯЕМ ТЕКУЩИЙ РАЗМЕР ИЗОБРАЖЕНИЯ
            // Ширина: от всей ширины экрана до collapsedSizePx
            val currentImgWidth = lerp(
                constraints.maxWidth.toFloat(),
                collapsedSizePx,
                collapsedFraction
            ).roundToInt()
            // Высота: от expandedHeightPx до collapsedSizePx
            val currentImgHeight =
                lerp(expandedHeightPx, collapsedSizePx, collapsedFraction).roundToInt()

            val imagePlaceable =
                measurables.firstOrNull { it.layoutId == ExpandedTitleId }?.measure(
                    constraints.copy(
                        minWidth = currentImgWidth,
                        maxWidth = currentImgWidth,
                        minHeight = currentImgHeight,
                        maxHeight = currentImgHeight
                    )
                )

            // 2. ВЫЧИСЛЯЕМ ВЫСОТУ ТУЛБАРА
            val collapsedHeightPx = MinCollapsedHeight.toPx()
            val layoutHeightPx = lerp(expandedHeightPx, collapsedHeightPx, collapsedFraction)

            // 3. КООРДИНАТЫ ИЗОБРАЖЕНИЯ
            // По X: от 0 до (середина между иконками или просто после навигации)

            // 1. Считаем "чистую" ширину иконки навигации с учетом отступа от края экрана
            val navigationFullWidth = navWidth + HorizontalPadding.toPx().roundToInt()

// 2. Целевая позиция X: это ширина иконки навигации + небольшой зазор (например, 8.dp)
            val imgGap = 8.dp.toPx()
            val imgTargetX = navigationFullWidth + imgGap

// 3. Рассчитываем текущий X
// В развернутом состоянии (collapsedFraction = 0) X = 0 (на весь экран)
// В свернутом (collapsedFraction = 1) X = imgTargetX (справа от кнопки "Назад")
            val imgX = lerp(0f, imgTargetX, collapsedFraction).roundToInt()

            // По Y: от 0 до центрирования в тулбаре
            val imgTargetY = (collapsedHeightPx - collapsedSizePx) / 2
            val imgY = lerp(0f, imgTargetY, collapsedFraction).roundToInt()

            // Настройка лимита скролла (чтобы знать, на сколько можно "задвинуть" тулбар)
            scrollBehavior?.state?.heightOffsetLimit = -(expandedHeightPx - collapsedHeightPx)

            layout(constraints.maxWidth, (layoutHeightPx + (additionalContentPlaceable?.height ?: 0)).roundToInt()) {
                // Сначала размещаем картинку (она будет на нижнем слое)
                imagePlaceable?.placeRelative(imgX, imgY)

                // Затем иконку навигации
                navigationIconPlaceable?.placeRelative(
                    x = HorizontalPadding.toPx().roundToInt(),
                    y = ((collapsedHeightPx - navigationIconPlaceable.height) / 2).roundToInt()
                )

                // Затем экшены
                actionsPlaceable?.placeRelative(
                    x = constraints.maxWidth - actionsPlaceable.width - HorizontalPadding.toPx().roundToInt(),
                    y = ((collapsedHeightPx - actionsPlaceable.height) / 2).roundToInt()
                )

                // И доп. контент
                additionalContentPlaceable?.placeRelative(
                    x = 0,
                    y = layoutHeightPx.roundToInt()
                )
            }
        }
    }
}


private fun lerp(a: Float, b: Float, fraction: Float): Float {
    return a + fraction * (b - a)
}

private val MinCollapsedHeight = 56.dp
private val HorizontalPadding = 16.dp
private val ExpandedTitleBottomPadding = 8.dp
private val CollapsedTitleLineHeight = 28.sp
private val DefaultCollapsedElevation = 4.dp

private const val ExpandedTitleId = "expandedTitle"
private const val CollapsedTitleId = "collapsedTitle"
private const val NavigationIconId = "navigationIcon"
private const val ActionsId = "actions"
private const val CentralContentId = "centralContent"
private const val AdditionalContentId = "additionalContent"