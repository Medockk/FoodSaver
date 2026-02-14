package com.foodsaver.app.presentation.featureHome.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = label
            )
        },
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (isSelected) com.foodsaver.app.ui.FoodSaverTheme.colorScheme.primary
            else com.foodsaver.app.ui.FoodSaverTheme.colorScheme.background,
            labelColor = if (isSelected) com.foodsaver.app.ui.FoodSaverTheme.colorScheme.background
            else com.foodsaver.app.ui.FoodSaverTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(1.dp, com.foodsaver.app.ui.FoodSaverTheme.colorScheme.primary)
    )
}