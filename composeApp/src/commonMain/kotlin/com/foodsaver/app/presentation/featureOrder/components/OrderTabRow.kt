package com.foodsaver.app.presentation.featureOrder.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.history
import foodsaver.composeapp.generated.resources.ongoing
import org.jetbrains.compose.resources.stringResource

@Composable
fun OrderTabRow(
    selectedTabIndex: Int,
    onTabIndexChange: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = FoodSaverTheme.colorScheme.background,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(selectedTabIndex),
                width = 150.dp,
                color = FoodSaverTheme.colorScheme.primary
            )
        },
    ) {
        Tab(
            selected = selectedTabIndex == 0,
            onClick = {
                onTabIndexChange(0)
            },
            text = {
                val isSelected = selectedTabIndex == 0
                Text(
                    text = stringResource(resource = Res.string.ongoing),
                    color = if (isSelected) FoodSaverTheme.colorScheme.primary
                    else FoodSaverTheme.colorScheme.placeholderHint,
                    style = if (isSelected) FoodSaverTheme.typography.bodyRegularBold
                    else FoodSaverTheme.typography.bodySmall
                )
            }
        )
        Tab(
            selected = selectedTabIndex == 1,
            onClick = {
                onTabIndexChange(1)
            },
            text = {
                val isSelected = selectedTabIndex == 1
                Text(
                    text = stringResource(resource = Res.string.history),
                    color = if (isSelected) FoodSaverTheme.colorScheme.primary
                    else FoodSaverTheme.colorScheme.placeholderHint,
                    style = if (isSelected) FoodSaverTheme.typography.bodyRegularBold
                    else FoodSaverTheme.typography.bodySmall
                )
            }
        )
    }
}