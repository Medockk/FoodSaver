package com.foodsaver.app.presentation.featureSearch.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.featureSearch.domain.model.RecentKeywordsModel
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.recent_keywords
import org.jetbrains.compose.resources.stringResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RecentKeywordsListPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold() { padding ->
            Box(Modifier.padding(padding)) {
                RecentKeywordsList(
                    keywords = listOf(),
                    onKeyworkClick = { TODO() }
                )
            }
        }
    }
}

@Composable
fun RecentKeywordsList(
    keywords: List<RecentKeywordsModel>,
    onKeyworkClick: (RecentKeywordsModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.recent_keywords),
            style = FoodSaverTheme.typography.bodyMedium,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
            modifier = Modifier
                .padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (keywords.isEmpty()) {
                items(6) {
                    Box(
                        Modifier
                            .size(90.dp, 50.dp)
                            .clip(RoundedCornerShape(33.dp))
                            .shimmerEffect()
                    )
                }
            } else {
                items(keywords) { word ->
                    OutlinedButton(
                        onClick = {
                            onKeyworkClick(word)
                        },
                        border = BorderStroke(
                            width = 2.dp,
                            color = FoodSaverTheme.colorScheme.unselectedChipBorderColor
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = word.value,
                            style = FoodSaverTheme.typography.bodyRegular,
                            color = FoodSaverTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}