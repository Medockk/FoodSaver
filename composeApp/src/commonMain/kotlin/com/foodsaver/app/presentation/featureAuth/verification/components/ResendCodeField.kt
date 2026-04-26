package com.foodsaver.app.presentation.featureAuth.verification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResendCodeFieldPreview() {
    LocalFoodSaverThemeComposition {
        ResendCodeField("1249", {})
    }
}

@Composable
fun ResendCodeField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    codeLength: Int = 4,
) {
    BasicTextField(
        value = value,
        onValueChange = {
            if (it.length <= codeLength && it.all { char -> char.isDigit() }) {
                onValueChange(it)
            }
        },
        modifier = modifier,
        textStyle = FoodSaverTheme.typography.bodyRegularBold.copy(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { _ ->
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                repeat(codeLength) { index ->
                    val char = when {
                        index >= value.length -> ""
                        else -> value[index].toString()
                    }

                    ResendCodeFieldItem(char)
                }
            }
        }
    )
}

@Composable
private fun ResendCodeFieldItem(value: String) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(FoodSaverTheme.colorScheme.placeholderBackground)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
            style = FoodSaverTheme.typography.bodyRegularBold.copy(fontSize = 16.sp)
        )
    }
}