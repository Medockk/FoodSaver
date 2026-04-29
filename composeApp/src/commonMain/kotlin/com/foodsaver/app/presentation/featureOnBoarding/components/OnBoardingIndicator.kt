package com.foodsaver.app.presentation.featureOnBoarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun OnBoardingIndicator(
    items: Int,
    currentItem: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        repeat(items) { index ->
            val background = if (currentItem == index) {
                FoodSaverTheme.colorScheme.primary
            } else {
                FoodSaverTheme.colorScheme.primaryThin
            }
            Box(
                modifier = Modifier.size(10.dp)
                    .background(background, CircleShape)
            )
        }
    }
}