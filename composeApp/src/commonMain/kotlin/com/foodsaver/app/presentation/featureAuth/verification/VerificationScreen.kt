package com.foodsaver.app.presentation.featureAuth.verification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.feature.auth.presentation.verification.VerificationEvent
import com.foodsaver.app.feature.auth.presentation.verification.VerificationState
import com.foodsaver.app.presentation.featureAuth.common.AuthenticationScaffold
import com.foodsaver.app.presentation.featureAuth.verification.components.ResendCodeField
import com.foodsaver.app.presentation.featureAuth.verification.components.ResendCodeItem
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.auth_verification_subtitle
import foodsaver.composeapp.generated.resources.auth_verification_title
import foodsaver.composeapp.generated.resources.auth_verification_verify
import org.jetbrains.compose.resources.stringResource

@Composable
fun VerificationScreenRoot(
    navController: NavController,
) {

    val code = remember { mutableStateOf("") }
    println("Verification Code ${code.value}")
    VerificationScreen(VerificationState(code = code.value), {
        when (it) {
            is VerificationEvent.OnCodeValueChange -> {
                code.value = it.value
            }
        }
    })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerificationScreenPreview() {
    LocalFoodSaverThemeComposition {
        VerificationScreen(VerificationState(), {})
    }
}

@Composable
private fun VerificationScreen(
    state: VerificationState,
    onEvent: (VerificationEvent) -> Unit
) {
    AuthenticationScaffold(
        title = Res.string.auth_verification_title,
        subtitle = Res.string.auth_verification_subtitle,
        onBackButtonClick = {},
        leftIconTint = FoodSaverTheme.colorScheme.primary.copy(.1f)
    ) {
        Column {
            ResendCodeItem(
                state.resendTimerValue,
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(Modifier.height(15.dp))

            ResendCodeField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.code,
                onValueChange = {
                    onEvent(VerificationEvent.OnCodeValueChange(it))
                }
            )

            Spacer(Modifier.height(30.dp))

            PrimaryButton(
                onClick = {
                },
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(Res.string.auth_verification_verify).uppercase()
            )
        }
    }
}