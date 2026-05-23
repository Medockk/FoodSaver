package com.foodsaver.app.presentation.featureFoodDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodsaver.app.featureFoodDetail.domain.model.IngredientAnalyzeResponse
import com.foodsaver.app.ui.FoodSaverTheme

fun LazyListScope.ingredientClassificationView(
    classifications: List<IngredientAnalyzeResponse>,
    modifier: Modifier = Modifier
) {
    items(classifications) { classification ->
        val dangerColor = when (classification.dangerLevel.lowercase()) {
            "высокий" -> FoodSaverTheme.colorScheme.deleteColor
            "средний" -> FoodSaverTheme.colorScheme.mainCategoryClipColor
            "низкий" -> FoodSaverTheme.colorScheme.completeColor
            else -> FoodSaverTheme.colorScheme.placeholderBackground
        }

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(24.dp).clip(CircleShape).background(dangerColor))
            Spacer(Modifier.width(15.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = classification.name,
                    color = FoodSaverTheme.colorScheme.onBackground,
                    style = FoodSaverTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee()
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = classification.explanation,
                    color = FoodSaverTheme.colorScheme.onBackgroundSecondary,
                    style = FoodSaverTheme.typography.bodySmall
                )
            }
        }

        Spacer(Modifier.height(10.dp))
    }
}