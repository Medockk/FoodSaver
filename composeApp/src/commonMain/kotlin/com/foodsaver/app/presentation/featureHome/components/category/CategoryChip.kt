package com.foodsaver.app.presentation.featureHome.components.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun CategoryChip(
    state: CategoryChipState,
    modifier: Modifier = Modifier,
) {

    val backgroundColor = if (state.isMainChip) {
        FoodSaverTheme.colorScheme.mainCategoryClipColor
    } else {
        FoodSaverTheme.colorScheme.background
    }

    val shadowColor = FoodSaverTheme.colorScheme.primaryShadowColor
    val shape = RoundedCornerShape(40.dp)

    Box(
        modifier = modifier
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    radius = 12.dp,
                    color = shadowColor,
                    alpha = .15f,
                    offset = DpOffset(x = 12.dp, 12.dp)
                )
            )
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = state.onCategoryClick
            )
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImageWithShimmerLoading(
                model = state.imageUri,
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape),
                shimmerDurationMillis = 7000
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = state.name,
                color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
                style = FoodSaverTheme.typography.bodyRegularBold
            )

            Spacer(Modifier.width(20.dp))
        }
    }
}