package com.foodsaver.app.presentation.featureHome.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.category_all_categories
import foodsaver.composeapp.generated.resources.category_see_all
import foodsaver.composeapp.generated.resources.see_all_categories_icon
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TableOfContent(
    text: StringResource,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
            style = FoodSaverTheme.typography.bodyMedium
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onSeeAllClick
                )
                .padding(5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.category_see_all),
                    color = FoodSaverTheme.colorScheme.categorySeeAllColor,
                    style = FoodSaverTheme.typography.bodyRegular
                )
                Spacer(Modifier.width(10.dp))
                Icon(
                    imageVector = vectorResource(Res.drawable.see_all_categories_icon),
                    contentDescription = null,
                    tint = FoodSaverTheme.colorScheme.onBackgroundTertiary,
                    modifier = Modifier
                        .size(5.dp, 10.dp)
                )
            }
        }
    }
}