package com.foodsaver.app.common.textField.fieldItem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.textField.PrimaryTextField
import com.foodsaver.app.common.textField.PrimaryTextFieldState
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.auth_email
import foodsaver.composeapp.generated.resources.auth_email_example
import org.jetbrains.compose.resources.stringResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TextFieldItemPreview() {
    TextFieldItem(
        state = TextFieldItemState(
            title = Res.string.auth_email,
            state = PrimaryTextFieldState(
                value = "",
                onValueChange = {  },
                placeholder = stringResource(Res.string.auth_email_example)
            )
        )
    )
}

@Composable
fun TextFieldItem(
    state: TextFieldItemState,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        Text(
            text = stringResource(state.title).uppercase(),
            style = FoodSaverTheme.typography.headerUppercase,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
        )

        Spacer(Modifier.height(8.dp))

        PrimaryTextField(
            state = state.state,
            modifier = modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))
    }
}