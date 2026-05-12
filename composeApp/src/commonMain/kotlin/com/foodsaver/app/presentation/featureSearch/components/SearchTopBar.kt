@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.featureSearch.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.common.topBar.CartTopBarIcon
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.filter_icon
import foodsaver.composeapp.generated.resources.search
import foodsaver.composeapp.generated.resources.search_icon
import foodsaver.composeapp.generated.resources.show_more_search_query_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SearchTopBar(
    navController: NavController,
    isFirstSearchingScreen: Boolean,
    searchQuery: String,
    cartItemValue: Long?,
    onCartIconClick: () -> Unit,
    onShowMoreSearchVariantsClick: () -> Unit,
    onSearchIconClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = FoodSaverTheme.colorScheme.background
        ),
        title = {
            Row {
                Spacer(Modifier.width(10.dp))
                if (isFirstSearchingScreen) {
                    Text(
                        text = stringResource(Res.string.search),
                        style = FoodSaverTheme.typography.bodyRegular,
                        color = FoodSaverTheme.colorScheme.onBackground
                    )
                } else {
                    OutlinedButton(
                        onClick = onShowMoreSearchVariantsClick,
                        border = BorderStroke(
                            width = 1.dp,
                            color = FoodSaverTheme.colorScheme.fabBackground
                        )
                    ) {
                        Text(
                            text = searchQuery,
                            style = FoodSaverTheme.typography.headerBoldSmall,
                            color = FoodSaverTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.width(7.dp))
                        Icon(
                            imageVector = vectorResource(Res.drawable.show_more_search_query_icon),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        },
        navigationIcon = {
            PrimaryFabButton(
                onClick = {
                    if (isFirstSearchingScreen) {
                        navController.navigateUp()
                    } else {
                        onSearchIconClick()
                    }
                }
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.back_icon),
                    contentDescription = null
                )
            }
        },
        actions = {
            if (isFirstSearchingScreen) {
                CartTopBarIcon(
                    cartItemValue = cartItemValue,
                    onCartClick = onCartIconClick
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = onSearchIconClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = FoodSaverTheme.colorScheme.backgroundContrast
                        ),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.search_icon),
                            contentDescription = null,
                            tint = FoodSaverTheme.colorScheme.onBackgroundContrast,
                            modifier = Modifier
                        )
                    }
                    IconButton(
                        onClick = onSearchIconClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = FoodSaverTheme.colorScheme.fabBackground
                        ),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.filter_icon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    )
}