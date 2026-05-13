package com.foodsaver.app.presentation.featurePaymentMethod.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.pay_and_confirm
import foodsaver.composeapp.generated.resources.total
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentMethodBottomBar(
    totalPrice: Double,
    currency: String,
    onPayClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val navigationPaddings = WindowInsets.navigationBars.asPaddingValues()
    Column(modifier.padding(navigationPaddings)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.total).uppercase(),
                style = FoodSaverTheme.typography.bodySmall,
                color = FoodSaverTheme.colorScheme.onBackgroundTertiary
            )

            Spacer(Modifier.width(15.dp))

            Text(
                text = "$totalPrice $currency",
                style = FoodSaverTheme.typography.bottomBarPrice,
                color = FoodSaverTheme.colorScheme.onBackground
            )
        }

        Spacer(Modifier.height(30.dp))

        PrimaryButton(
            onClick = onPayClick,
            text = stringResource(Res.string.pay_and_confirm),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}