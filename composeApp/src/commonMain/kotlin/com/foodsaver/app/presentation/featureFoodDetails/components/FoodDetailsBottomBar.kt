package com.foodsaver.app.presentation.featureFoodDetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.ProductCounter
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_to_cart
import org.jetbrains.compose.resources.stringResource

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
            ProductCounter(
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