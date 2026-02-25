package com.foodsaver.app.presentation.featureHome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.coreModel.model.CategoryModel
import com.foodsaver.app.presentation.Home.HomeEvent

@Composable
fun CategoryHeader(
    categories: List<CategoryModel>,
    isCategoriesLoading: Boolean,
    selectedCategoryIds: Set<String>,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (isCategoriesLoading && categories.isEmpty()) {
                items(6, key = { index -> index }) {
                    Box(
                        Modifier.size(70.dp, 30.dp).clip(RoundedCornerShape(20.dp))
                            .shimmerEffect()
                    )
                }
            } else {
                items(
                    items = categories,
                    key = { it.categoryId }
                ) { category ->
                    val isSelected = remember(selectedCategoryIds) {
                        selectedCategoryIds.contains(category.categoryId)
                    }
                    CategoryChip(
                        label = category.categoryName,
                        isSelected = isSelected,
                        onClick = {
                            onCategoryClick(category.categoryId)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))
    }
}