package com.foodsaver.app.presentation.featureFoodDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun SizeView(
    size: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .sizeIn(50.dp, 50.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) FoodSaverTheme.colorScheme.primary
                else FoodSaverTheme.colorScheme.placeholderBackground
            ).clickable(onClick = onClick).padding(12.dp),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = size,
            color = if (isSelected) Color.White
            else FoodSaverTheme.colorScheme.backgroundContrast,
            style = FoodSaverTheme.typography.bodyRegular.copy(
                fontWeight = if (isSelected) FontWeight.Bold
                else FontWeight.Normal
            )
        )
    }
}