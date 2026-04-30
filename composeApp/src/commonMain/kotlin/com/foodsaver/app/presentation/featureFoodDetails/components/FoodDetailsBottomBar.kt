package com.foodsaver.app.presentation.featureFoodDetails.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_icon
import foodsaver.composeapp.generated.resources.add_to_cart
import foodsaver.composeapp.generated.resources.remove_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun FoodDetailsBottomBar(
    price: Double,
    productCount: Int,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    onAddProductToCart: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .padding(top = 20.dp)
            .padding(start = 35.dp, end = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = price.toString(),
                style = FoodSaverTheme.typography.bottomBarPrice,
                color = FoodSaverTheme.colorScheme.onBackground
            )

            Spacer(Modifier.weight(1f))
            // Counter
            Counter(
                productCount = productCount,
                onIncreaseClick = onIncreaseClick,
                onDecreaseClick = onDecreaseClick
            )
        }

        Spacer(Modifier.height(25.dp))
        PrimaryButton(
            onClick = onAddProductToCart,
            text = stringResource(Res.string.add_to_cart),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun Counter(
    productCount: Int,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(FoodSaverTheme.colorScheme.counterColor)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(FoodSaverTheme.colorScheme.counterButtonColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onDecreaseClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.remove_icon),
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(Modifier.width(20.dp))

        Text(
            text = productCount.toString(),
            style = FoodSaverTheme.typography.headerRegularBold,
            color = Color.White
        )

        Spacer(Modifier.width(20.dp))
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(FoodSaverTheme.colorScheme.counterButtonColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onIncreaseClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.add_icon),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}