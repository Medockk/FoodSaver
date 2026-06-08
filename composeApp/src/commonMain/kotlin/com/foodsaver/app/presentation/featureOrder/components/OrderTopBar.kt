@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.featureOrder.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
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
import foodsaver.composeapp.generated.resources.more_icon
import foodsaver.composeapp.generated.resources.my_orders
import foodsaver.composeapp.generated.resources.qrcode_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OrderTopBar(
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    TopAppBar(
        modifier = modifier,
        title = {
            Row {
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(Res.string.my_orders),
                    color = FoodSaverTheme.colorScheme.onBackground,
                    style = FoodSaverTheme.typography.bodyRegular
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
        },
        actions = {
            PrimaryFabButton(
                onClick = onMoreClick
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.qrcode_icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = FoodSaverTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = FoodSaverTheme.colorScheme.background
        )
    )
}