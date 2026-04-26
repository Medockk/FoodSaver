package com.foodsaver.app.presentation.featureAuth.common.fieldItem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.presentation.featureAuth.common.textField.AuthenticationTextField
import com.foodsaver.app.presentation.featureAuth.common.textField.AuthenticationTextFieldState
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.auth_email
import foodsaver.composeapp.generated.resources.auth_email_example
import org.jetbrains.compose.resources.stringResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthenticationItemPreview() {
    AuthenticationItem(
        state = AuthenticationItemState(
            title = Res.string.auth_email,
            state = AuthenticationTextFieldState(
                value = "",
                onValueChange = {  },
                placeholder = Res.string.auth_email_example
            )
        )
    )
}

@Composable
fun AuthenticationItem(
    state: AuthenticationItemState,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        Text(
            text = stringResource(state.title).uppercase(),
            style = FoodSaverTheme.typography.headerUppercase,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
        )

        Spacer(Modifier.height(8.dp))

        AuthenticationTextField(
            state = state.state,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))
    }
}