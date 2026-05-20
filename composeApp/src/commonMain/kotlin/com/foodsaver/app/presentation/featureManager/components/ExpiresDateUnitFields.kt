package com.foodsaver.app.presentation.featureManager.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductState
import com.foodsaver.app.common.textField.BorderTextField
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.utils.date.FullDateVisualTransformation
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_product_placeholder_expires_date
import foodsaver.composeapp.generated.resources.unit_type
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExpiresDateUnitFields(
    expiresDate: String,
    onExpiresDateChange: (String) -> Unit,
    selectedUnit: AddProductState.Unit?,
    onPickUnit: (AddProductState.Unit) -> Unit,
    modifier: Modifier = Modifier
) {

    var isUnitDropdownMenuExpanded by retain { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BorderTextField(
            value = expiresDate,
            onValueChange = onExpiresDateChange,
            modifier = Modifier
                .weight(1f),
            placeholder = stringResource(Res.string.add_product_placeholder_expires_date),
            visualTransformation = FullDateVisualTransformation(),
            keyboardType = KeyboardType.Number,
            innerPadding = 0.dp
        )

        Spacer(Modifier.width(5.dp))
        Box {
            OutlinedButton(
                onClick = {
                    isUnitDropdownMenuExpanded = !isUnitDropdownMenuExpanded
                },
                shape = RectangleShape,
                modifier = Modifier
                    .height(IntrinsicSize.Min)
            ) {
                if (selectedUnit != null) {
                    Text(
                        text = selectedUnit.name,
                        style = FoodSaverTheme.typography.bodySmall,
                        color = FoodSaverTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.unit_type),
                        style = FoodSaverTheme.typography.bodySmall,
                        color = FoodSaverTheme.colorScheme.primary
                    )
                }
            }
            DropdownMenu(
                expanded = isUnitDropdownMenuExpanded,
                onDismissRequest = {
                    isUnitDropdownMenuExpanded = false
                }
            ) {

                AddProductState.Unit.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = unit.name
                            )
                        },
                        onClick = {
                            onPickUnit(unit)
                            isUnitDropdownMenuExpanded = false
                        }
                    )
                }
            }

        }
    }
}