@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.featurePaymentMethod.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.payment
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PaymentMethodTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = FoodSaverTheme.colorScheme.background
        ),
        title = {
            Row {
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(Res.string.payment),
                    style = FoodSaverTheme.typography.bodyRegular,
                    color = FoodSaverTheme.colorScheme.onBackground
                )
            }
        },
        navigationIcon = {
            PrimaryFabButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.back_icon),
                    contentDescription = null,
                    tint = FoodSaverTheme.colorScheme.onBackground
                )
            }
        }
    )
}