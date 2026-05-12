package com.foodsaver.app.presentation.featureSearch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.star_icon
import foodsaver.composeapp.generated.resources.suggested_restaurants
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SuggestedRestaurantsPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(Modifier.padding(padding)) {
                SuggestedRestaurants(
                    restaurants = listOf(
                        RestaurantModel(
                            id = "dd",
                            name = "Qwe",
                            description = "asd lfkp kklsp [wpkwm wjwjd vhsi ajeo jeq jcnmks woemf",
                            longitude = 0.0,
                            latitude = 0.0,
                            addressName = "Address",
                            companyId = ""
                        ),
                        RestaurantModel(
                            id = "dd",
                            name = "Qwe",
                            description = "asd lfkp kklsp [wpkwm wjwjd vhsi ajeo jeq jcnmks woemf",
                            longitude = 0.0,
                            latitude = 0.0,
                            addressName = "Address",
                            companyId = "",
                            rating = 4.5
                        ),
                    ),
                    onRestaurantClick = { TODO() }
                )
            }
        }
    }
}
@Composable
fun SuggestedRestaurants(
    restaurants: List<RestaurantModel>,
    onRestaurantClick: (RestaurantModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.suggested_restaurants),
            style = FoodSaverTheme.typography.bodyMedium,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
        )
        Spacer(Modifier.height(20.dp))

        restaurants.forEach { restaurant ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = {
                            onRestaurantClick(restaurant)
                        }
                    )
            ) {
                AsyncImageWithShimmerLoading(
                    model = restaurant.photoUris,
                    modifier = Modifier
                        .size(60.dp, 50.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        text = restaurant.name,
                        color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
                        style = FoodSaverTheme.typography.bodyRegular
                    )
                    Spacer(Modifier.height(8.dp))
                    restaurant.rating?.let { rating ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.star_icon),
                                contentDescription = null,
                                tint = FoodSaverTheme.colorScheme.primary
                            )
                            Text(
                                text = rating.toString(),
                                color = FoodSaverTheme.colorScheme.onBackground,
                                style = FoodSaverTheme.typography.bodyRegular
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(15.dp))

            HorizontalDivider(color = FoodSaverTheme.colorScheme.dividerLineColor)

            Spacer(Modifier.height(15.dp))
        }
    }
}