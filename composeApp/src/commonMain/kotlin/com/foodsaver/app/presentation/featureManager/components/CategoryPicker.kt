package com.foodsaver.app.presentation.featureManager.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun CategoryPicker(
    allCategories: List<CategoryModel>,
    selectedCategoryIds: List<String>,
    onPickCategory: (CategoryModel) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(allCategories) { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    onPickCategory(category)
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = FoodSaverTheme.colorScheme.onBackgroundSecondary,
                            shape = CircleShape
                        ).then(
                            other = if (selectedCategoryIds.contains(category.categoryId)) {
                                Modifier.background(FoodSaverTheme.colorScheme.primary.copy(.5f))
                            } else {
                                Modifier
                            }
                        )
                )
                Spacer(Modifier.height(5.dp))

                Text(
                    text = category.categoryName,
                    style = FoodSaverTheme.typography.ingredientName,
                    color = FoodSaverTheme.colorScheme.onBackgroundThin
                )
            }
        }
    }
}