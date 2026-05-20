package com.foodsaver.app.common.textField.fieldItem.createLabel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun CreateLabel(
    label: String,
    modifier: Modifier = Modifier
) {
    Column {
        Spacer(Modifier.height(20.dp))
        Text(
            text = label,
            color = FoodSaverTheme.colorScheme.onBackground,
            style = FoodSaverTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(16.dp))
    }
}