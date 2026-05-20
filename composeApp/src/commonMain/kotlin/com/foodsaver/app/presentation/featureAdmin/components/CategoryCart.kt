package com.foodsaver.app.presentation.featureAdmin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryIconButton
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.delete_icon

@Composable
fun CategoryCart(
    category: CategoryModel,
    onCategoryClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = FoodSaverTheme.colorScheme.placeholderBackground
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
        onClick = onCategoryClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(5.dp)
        ) {
            Row {
                Text(
                    text = category.categoryName,
                    color = FoodSaverTheme.colorScheme.onPlaceholderBackgroundActive,
                    style = FoodSaverTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
                )

                PrimaryIconButton(
                    onClick = onDeleteClick,
                    icon = Res.drawable.delete_icon
                )
            }
        }
    }
}