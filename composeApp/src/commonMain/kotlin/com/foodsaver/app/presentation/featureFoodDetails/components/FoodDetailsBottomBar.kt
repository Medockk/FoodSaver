package com.foodsaver.app.presentation.featureFoodDetails.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodsaver.app.common.product.ProductCounter
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import com.foodsaver.app.utils.format.format2
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_to_cart
import foodsaver.composeapp.generated.resources.remove_from_cart
import org.jetbrains.compose.resources.stringResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FoodDetailsBottomBarPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(Modifier.padding(padding)) {
                FoodDetailsBottomBar(
                    price = 250.0,
                    discount = 0.12,
                    productCount = 123,
                    isProductInCart = true,
                    onIncreaseClick = { TODO() },
                    onDecreaseClick = { TODO() },
                    onAddProductToCart = { TODO() },
                    onRemoveProductFromCart = { TODO() },
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun FoodDetailsBottomBar(
    price: Double,
    discount: Double,
    productCount: Long,
    isProductInCart: Boolean,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    onAddProductToCart: () -> Unit,
    onRemoveProductFromCart: () -> Unit,
    modifier: Modifier = Modifier
) {

    val oldPrice = remember(price) { price }
    val actualPrice by remember(price, discount) {
        mutableStateOf(price - (price * discount))
    }

    Column(
        modifier = modifier
            .padding(top = 20.dp)
            .padding(start = 35.dp, end = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (discount > 0) {
                Text(
                    text = oldPrice.toString(),
                    fontSize = 20.sp,
                    style = FoodSaverTheme.typography.bodyMedium,
                    color = FoodSaverTheme.colorScheme.deleteColor,
                    textDecoration = TextDecoration.LineThrough
                )
                Spacer(Modifier.width(10.dp))
            }
            Text(
                text = actualPrice.format2(),
                fontSize = 28.sp,
                style = FoodSaverTheme.typography.bodyBold,
                color = FoodSaverTheme.colorScheme.onBackground
            )

            Spacer(Modifier.weight(1f))
            // Counter
            ProductCounter(
                productCount = productCount,
                onIncreaseClick = onIncreaseClick,
                onDecreaseClick = onDecreaseClick
            )
        }

        Spacer(Modifier.height(25.dp))
        PrimaryButton(
            onClick = {
                if (isProductInCart) onRemoveProductFromCart()
                else onAddProductToCart()
            },
            text = if (isProductInCart) stringResource(Res.string.remove_from_cart)
            else stringResource(Res.string.add_to_cart),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}