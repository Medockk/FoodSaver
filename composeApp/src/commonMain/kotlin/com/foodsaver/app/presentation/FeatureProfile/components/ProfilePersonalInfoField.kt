package com.foodsaver.app.presentation.FeatureProfile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ProfilePersonalInfoField(
    val label: String,
    val placeholder: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val maxLines: Int = 1,
    val imeAction: ImeAction = ImeAction.Next,
    val imeOnAny: (KeyboardActionScope.() -> Unit) = {},
)

@Composable
fun ProfilePersonalInfoField(
    profilePersonalInfoField: ProfilePersonalInfoField,
    modifier: Modifier = Modifier,
) {
    with(profilePersonalInfoField) {
        Column {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onTertiary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(Modifier.height(8.dp))

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                placeholder = {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                },
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                maxLines = maxLines,
                keyboardActions = KeyboardActions(
                    onAny = imeOnAny
                )
            )
        }
    }
}