@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.featureHome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.common.topBar.CartTopBarIcon
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.arrow_down_icon
import foodsaver.composeapp.generated.resources.home_topbar_deliver_to
import foodsaver.composeapp.generated.resources.menu_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeTopBarPreview() {
    LocalFoodSaverThemeComposition {
        HomeTopBar("SomeWhere", 10, {}, {})
    }
}

@Composable
fun HomeTopBar(
    deliverTo: String,
    cartItemValue: Long?,
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    TopAppBar(
        modifier = modifier,
        title = {
            Column(Modifier.padding(start = 20.dp)) {
                Text(
                    text = stringResource(Res.string.home_topbar_deliver_to),
                    color = FoodSaverTheme.colorScheme.primary,
                    style = FoodSaverTheme.typography.headerBoldSmall
                )
                Spacer(Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = deliverTo,
                        color = FoodSaverTheme.colorScheme.topBarSubtitleColor,
                        style = FoodSaverTheme.typography.bodySmall
                    )
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        imageVector = vectorResource(Res.drawable.arrow_down_icon),
                        contentDescription = null,
                        tint = FoodSaverTheme.colorScheme.onBackground
                    )
                }
            }
        },
        navigationIcon = {
            PrimaryFabButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.menu_icon),
                    contentDescription = null,
                    tint = FoodSaverTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            CartTopBarIcon(
                cartItemValue = cartItemValue,
                onCartClick = onCartClick
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = FoodSaverTheme.colorScheme.background
        )
    )
}