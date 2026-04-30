package com.foodsaver.app.common.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PrimaryChipPreview() {
    LocalFoodSaverThemeComposition {
        LazyRow {
            items(10) {
                PrimaryChip("Some title $it", it == 1, {})
            }
        }
    }
}

@Composable
fun PrimaryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = FoodSaverTheme.typography.bodyRegular
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(33.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = FoodSaverTheme.colorScheme.background,
            selectedContainerColor = FoodSaverTheme.colorScheme.primary,
            labelColor = FoodSaverTheme.colorScheme.onBackground,
            selectedLabelColor = Color.White,
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) FoodSaverTheme.colorScheme.primary
            else FoodSaverTheme.colorScheme.unselectedChipBorderColor
        ),
        elevation = FilterChipDefaults.elevatedFilterChipElevation(0.dp)
    )
}