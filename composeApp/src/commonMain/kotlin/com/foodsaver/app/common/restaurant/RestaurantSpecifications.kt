package com.foodsaver.app.common.restaurant

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.clock_icon
import foodsaver.composeapp.generated.resources.delivery_free
import foodsaver.composeapp.generated.resources.delivery_icon
import foodsaver.composeapp.generated.resources.star_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun RestaurantSpecifications(
    rating: Double?,
    deliveryCost: Double?,
    averageDeliveryTime: Double?,
    modifier: Modifier = Modifier,
    spaceBetweenSpecification: Dp = 35.dp,
    spaceBetweenItems: Dp = 10.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        rating?.let { rating ->
            Icon(
                imageVector = vectorResource(Res.drawable.star_icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(spaceBetweenItems))
            Text(
                text = rating.toString(),
                style = FoodSaverTheme.typography.bodyRegular,
                color = FoodSaverTheme.colorScheme.onBackground
            )

            Spacer(Modifier.width(spaceBetweenSpecification))
        }

        Icon(
            imageVector = vectorResource(Res.drawable.delivery_icon),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(Modifier.width(spaceBetweenItems))
        if (deliveryCost == null) {
            Text(
                text = stringResource(Res.string.delivery_free),
                style = FoodSaverTheme.typography.bodyRegular,
                color = FoodSaverTheme.colorScheme.onBackground
            )
        } else {
            Text(
                text = deliveryCost.toString(),
                style = FoodSaverTheme.typography.bodyRegular,
                color = FoodSaverTheme.colorScheme.onBackground
            )
        }
        Spacer(Modifier.width(spaceBetweenSpecification))


        averageDeliveryTime?.let { averageDeliveryTime ->
            Icon(
                imageVector = vectorResource(Res.drawable.clock_icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(spaceBetweenItems))
            Text(
                text = averageDeliveryTime.toString(),
                style = FoodSaverTheme.typography.bodyRegular,
                color = FoodSaverTheme.colorScheme.onBackground
            )
        }
    }
}