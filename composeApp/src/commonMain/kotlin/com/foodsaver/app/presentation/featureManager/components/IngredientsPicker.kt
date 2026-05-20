package com.foodsaver.app.presentation.featureManager.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.ingredient.IngredientView
import com.foodsaver.app.coreIngredients.domain.model.IngredientModel

@Composable
fun IngredientsPicker(
    ingredients: List<IngredientModel>,
    onIngredientClick: (IngredientModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(ingredients) { ingredient ->
            IngredientView(
                ingredient = ingredient,
                modifier = Modifier.clickable {
                    onIngredientClick(ingredient)
                }
            )
        }
    }
}