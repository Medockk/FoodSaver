package com.foodsaver.app.common.dropdownMenu

import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.more_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PrimaryDropdownMenu(
    dropdownMenuItems: List<PrimaryDropdownMenuState>,
    modifier: Modifier = Modifier,
) {
    var isDropdownMenuVisible by retain { mutableStateOf(false) }

    Box {
        PrimaryFabButton(
            onClick = {
                isDropdownMenuVisible = !isDropdownMenuVisible
            }
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.more_icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        androidx.compose.animation.AnimatedVisibility(
            visible = isDropdownMenuVisible,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            DropdownMenu(
                expanded = isDropdownMenuVisible,
                onDismissRequest = { isDropdownMenuVisible = false },
                shape = CircleShape,
                modifier = modifier,
                containerColor = FoodSaverTheme.colorScheme.background
            ) {
                dropdownMenuItems.forEach { item ->
                    DropdownMenuItem(
                        text = item.item,
                        onClick = {
                            isDropdownMenuVisible = false
                            item.onItemClick()
                        },
                    )
                }
            }
        }
    }

}