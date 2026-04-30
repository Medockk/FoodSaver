package com.foodsaver.app.presentation.featureRestaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ImagePageIndicatorPreview() {
    LocalFoodSaverThemeComposition {
        ImagePageIndicator(
            items = 5,
            currentPosition = 2
        )
    }
}

@Composable
fun ImagePageIndicator(
    items: Int,
    currentPosition: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(items) { item ->
            if (item == currentPosition) {
                // selected big indicator
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = FoodSaverTheme.colorScheme.imagePageSelectedIndicatorColor,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(FoodSaverTheme.colorScheme.imagePageSelectedIndicatorColor)
                    )
                }
            } else {
                // unselected indicator
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(FoodSaverTheme.colorScheme.imagePageUnselectedIndicatorColor)
                )
            }
        }
    }
}