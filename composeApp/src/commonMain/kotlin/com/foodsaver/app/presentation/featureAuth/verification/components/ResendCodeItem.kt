package com.foodsaver.app.presentation.featureAuth.verification.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.auth_verification_code
import foodsaver.composeapp.generated.resources.auth_verification_in
import foodsaver.composeapp.generated.resources.auth_verification_resend
import foodsaver.composeapp.generated.resources.auth_verification_secound
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResendCodeItem(
    timerValue: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.auth_verification_code),
            style = FoodSaverTheme.typography.headerUppercase,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
        )
        Spacer(Modifier.weight(1f))

        Text(
            text = stringResource(Res.string.auth_verification_resend),
            style = FoodSaverTheme.typography.bodyRegularBold,
            textDecoration = TextDecoration.Underline,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
        )
        Text(
            text = stringResource(Res.string.auth_verification_in) + " $timerValue" +
                stringResource(Res.string.auth_verification_secound),
            style = FoodSaverTheme.typography.bodySmall,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
        )
    }
}