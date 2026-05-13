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
import foodsaver.composeapp.generated.resources.add_card
import foodsaver.composeapp.generated.resources.close_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AddCardTopBar(
    onCloseIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row {
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(Res.string.add_card),
                    style = FoodSaverTheme.typography.bodyRegular,
                    color = FoodSaverTheme.colorScheme.onBackground
                )
            }
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = FoodSaverTheme.colorScheme.background
        ),
        navigationIcon = {
            PrimaryFabButton(
                onClick = onCloseIconClick,
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.close_icon),
                    contentDescription = null,
                    tint = FoodSaverTheme.colorScheme.onBackground
                )
            }
        }
    )
}