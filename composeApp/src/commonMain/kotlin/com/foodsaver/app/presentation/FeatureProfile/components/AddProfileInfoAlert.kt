package com.foodsaver.app.presentation.FeatureProfile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.save
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private enum class AddProfileInfoAlertIds {
    GLOBAL_CONTENT_ID,
    DIALOG_ID,
}

@Preview(showBackground = true, locale = "en", backgroundColor = 0xffffffff)
@Composable
fun AddProfileInfoAlertPreview() {
    AddProfileInfoAlert(
        content = {
            TextField(
                "", {},
                placeholder = {
                    Text(stringResource(Res.string.save))
                })
        },
        onSaveButtonClick = {},
        onDismissRequestClick = {}
    )
}

@Composable
fun AddProfileInfoAlert(
    content: @Composable () -> Unit,
    onSaveButtonClick: () -> Unit,
    onDismissRequestClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val dialogContent = @Composable {
        Column(
            modifier = modifier
                .layoutId(AddProfileInfoAlertIds.DIALOG_ID)
                .fillMaxWidth(0.7f)
                .dropShadow(RoundedCornerShape(15.dp), Shadow(4.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(15.dp))
                .clickable(remember { MutableInteractionSource() }, null, onClick = {})
                .padding(10.dp)
        ) {
            content()
            TextButton(
                onClick = onSaveButtonClick,
                modifier = Modifier
                    .align(Alignment.End),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(Res.string.save)
                )
            }
        }
    }
    val globalContent = @Composable {
        Box(
            Modifier
                .layoutId(AddProfileInfoAlertIds.GLOBAL_CONTENT_ID)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onDismissRequestClick()
                }
        )
    }

    Layout(
        content = {
            globalContent()
            dialogContent()
        }
    ) { measurables, constraints ->

        val globalContentPlaceable =
            measurables.find { it.layoutId == AddProfileInfoAlertIds.GLOBAL_CONTENT_ID }!!
                .measure(constraints)

        val dialogPlaceable = measurables.find { it.layoutId == AddProfileInfoAlertIds.DIALOG_ID }!!
            .measure(constraints)

        val maxWidth = globalContentPlaceable.width
        val maxHeight = globalContentPlaceable.height

        layout(maxWidth, maxHeight) {
            globalContentPlaceable.placeRelative(0, 0)

            val dialogWidth = dialogPlaceable.width
            val dialogHeight = dialogPlaceable.height

            val xDialogPosition = (maxWidth / 2) - (dialogWidth / 2)
            val yDialogPosition =( maxHeight / 2) - (dialogHeight / 2)

            dialogPlaceable.placeRelative(xDialogPosition, yDialogPosition, zIndex = 1f)
        }
    }
}