@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.common.topBar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PrimaryTopBar(
    title: String?,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: DrawableResource = Res.drawable.back_icon,
    navigationIconBackground: Color = FoodSaverTheme.colorScheme.fabBackground,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = FoodSaverTheme.colorScheme.background
        ),
        title = {
           title?.let { title ->
               Row {
                   Spacer(Modifier.width(10.dp))
                   Text(
                       text = title,
                       color = FoodSaverTheme.colorScheme.onBackground,
                       style = FoodSaverTheme.typography.bodyRegular
                   )
               }
           }
        },
        navigationIcon = {
            PrimaryFabButton(
                onClick = onNavigationClick,
                background = navigationIconBackground
            ) {
                Icon(
                    imageVector = vectorResource(navigationIcon),
                    contentDescription = null,
                    tint = FoodSaverTheme.colorScheme.onBackground
                )
            }
        },
        actions = { actions?.invoke(this) }
    )
}