package com.foodsaver.app.common.topBar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.cart_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun CartTopBarIcon(
    cartItemValue: Long?,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        PrimaryFabButton(
            onClick = onCartClick,
            background = FoodSaverTheme.colorScheme.onBackground
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.cart_icon),
                contentDescription = null,
                tint = FoodSaverTheme.colorScheme.background,
                modifier = Modifier
                    .size(20.dp)
            )
        }

        if (cartItemValue != null && cartItemValue > 0) {

            Badge(
                containerColor = FoodSaverTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = cartItemValue.toString(),
                    modifier = Modifier
                )
            }
        }
    }
}