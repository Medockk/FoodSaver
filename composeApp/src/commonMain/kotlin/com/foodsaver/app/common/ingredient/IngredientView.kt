package com.foodsaver.app.common.ingredient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.foodsaver.app.coreIngredients.domain.model.IngredientModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ingredients_allergy
import org.jetbrains.compose.resources.stringResource

@Composable
fun IngredientView(
    ingredient: IngredientModel,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .sizeIn(50.dp, 50.dp)
                .clip(CircleShape)
                .background(FoodSaverTheme.colorScheme.ingredientBackgroundColor)
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ingredient.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
        }

        Spacer(Modifier.height(5.dp))

        Text(
            text = ingredient.name,
            style = FoodSaverTheme.typography.ingredientName,
            color = FoodSaverTheme.colorScheme.onBackgroundThin
        )
        if (ingredient.isAllergy) {
            Text(
                text = "(" + stringResource(Res.string.ingredients_allergy) + ")",
                style = FoodSaverTheme.typography.ingredientsSubtext,
                color = FoodSaverTheme.colorScheme.onBackgroundThin
            )
        }
    }
}