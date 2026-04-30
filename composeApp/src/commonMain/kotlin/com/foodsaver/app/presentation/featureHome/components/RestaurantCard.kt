package com.foodsaver.app.presentation.featureHome.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.foodsaver.app.coreModel.model.OrganizationModel
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.clock_icon
import foodsaver.composeapp.generated.resources.delivery_free
import foodsaver.composeapp.generated.resources.delivery_icon
import foodsaver.composeapp.generated.resources.star_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RestaurantCardPreview() {
    LocalFoodSaverThemeComposition {
        RestaurantCard(
            RestaurantModel(
                id = "",
                latitude = 2.3,
                longitude = 2.5,
                addressName = "Address name...",
                organization = OrganizationModel("", "Magnit"),
                description = "Some description...",
                rating = null,
                deliveryCost = null,
                averageDeliveryTime = null,
                name = "Some burgers",
                photoUris = listOf(),
            ),
            onRestaurantClick = { }
        )
    }
}

@Composable
fun RestaurantCard(
    restaurant: RestaurantModel,
    onRestaurantClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onRestaurantClick
            )
            .padding(5.dp),
    ) {
        AsyncImageWithShimmerLoading(
            model = restaurant.photoUris.firstOrNull(),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 130.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = restaurant.organization.organizationName,
            color = FoodSaverTheme.colorScheme.onBackground,
            style = FoodSaverTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = restaurant.description,
            color = FoodSaverTheme.colorScheme.onBackgroundTertiary,
            style = FoodSaverTheme.typography.bodySmall
        )

        Spacer(Modifier.height(15.dp))

        // restaurant specifications
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            restaurant.rating?.let { rating ->
                Icon(
                    imageVector = vectorResource(Res.drawable.star_icon),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    text = rating.toString(),
                    style = FoodSaverTheme.typography.bodyRegular,
                    color = FoodSaverTheme.colorScheme.onBackground
                )

                Spacer(Modifier.width(24.dp))
            }

            Icon(
                imageVector = vectorResource(Res.drawable.delivery_icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
            if (restaurant.deliveryCost == null) {
                Text(
                    text = stringResource(Res.string.delivery_free),
                    style = FoodSaverTheme.typography.bodyRegular,
                    color = FoodSaverTheme.colorScheme.onBackground
                )
            } else {
                Text(
                    text = restaurant.deliveryCost.toString(),
                    style = FoodSaverTheme.typography.bodyRegular,
                    color = FoodSaverTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.width(24.dp))


            restaurant.averageDeliveryTime?.let { averageDeliveryTime ->
                Icon(
                    imageVector = vectorResource(Res.drawable.clock_icon),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    text = averageDeliveryTime.toString(),
                    style = FoodSaverTheme.typography.bodyRegular,
                    color = FoodSaverTheme.colorScheme.onBackground
                )
            }
        }
    }
}