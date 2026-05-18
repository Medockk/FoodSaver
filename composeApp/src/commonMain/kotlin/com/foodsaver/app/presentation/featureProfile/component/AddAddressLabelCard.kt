package com.foodsaver.app.presentation.featureProfile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun AddAddressLabelCard(
    isSelected: Boolean,
    label: String,
    onClick: () -> Unit,
    minWidth: Dp = 95.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .widthIn(minWidth)
            .clip(RoundedCornerShape(22.5.dp))
            .background(if (isSelected) FoodSaverTheme.colorScheme.primary else FoodSaverTheme.colorScheme.backgroundSecondary)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) FoodSaverTheme.colorScheme.onPrimary
            else FoodSaverTheme.colorScheme.onBackgroundSecondary,
            style = FoodSaverTheme.typography.bodySmall
        )
    }
}