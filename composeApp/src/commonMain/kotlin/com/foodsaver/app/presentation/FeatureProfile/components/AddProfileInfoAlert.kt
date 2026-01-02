package com.foodsaver.app.presentation.FeatureProfile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.save
import org.jetbrains.compose.resources.stringResource

private enum class AddProfileInfoAlertIds {
    DIALOG_ID,
    SAVE_BUTTON_ID,
}

@Composable
fun AddProfileInfoAlert(
    content: @Composable () -> Unit,
    onSaveButtonClick: () -> Unit,
    onDismissRequestClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val saveButton = @Composable {
        TextButton(
            onClick = onSaveButtonClick,
            modifier = Modifier
                .layoutId(AddProfileInfoAlertIds.SAVE_BUTTON_ID),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(Res.string.save)
            )
        }
    }
    val dialogContent = @Composable {
        Box(
            modifier = modifier
                .layoutId(AddProfileInfoAlertIds.DIALOG_ID)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center
        ) {
            Dialog(
                onDismissRequest = onDismissRequestClick,
                content = content
            )
        }
    }

    Layout(
        content = {
            dialogContent()
            saveButton()
        }
    ) { measurables, constraints ->

        val saveButton = (measurables.find { it.layoutId == AddProfileInfoAlertIds.SAVE_BUTTON_ID } ?: throw Exception("button"))
            .measure(constraints)

        val dialog = (measurables.find { it.layoutId == AddProfileInfoAlertIds.DIALOG_ID } ?: throw Exception("dialog"))
            .measure(constraints)

        val maxWidth = dialog.width
        val maxHeight = dialog.height + saveButton.height

        layout(maxWidth, maxHeight) {
            dialog.placeRelative(0, 0)
            println("Max $maxWidth, $maxHeight")

            val xSaveButtonPosition = maxWidth - (maxWidth / 4)
            val ySaveButtonPosition = maxHeight - (maxHeight / 10)
            println("Coordinates $xSaveButtonPosition $ySaveButtonPosition")

            saveButton.placeRelative(xSaveButtonPosition, ySaveButtonPosition)
        }
    }
}