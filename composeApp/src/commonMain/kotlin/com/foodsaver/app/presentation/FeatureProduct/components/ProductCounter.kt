package com.foodsaver.app.presentation.FeatureProduct.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodsaver.app.common.CircularPrimaryButton
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_minus_icon
import foodsaver.composeapp.generated.resources.ic_plus_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProductCounter(
    count: Int,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CircularPrimaryButton(
            onClick = onDecreaseClick,
            backgroundColor = Color.Transparent,
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primaryFixedDim)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_minus_icon),
                contentDescription = "Decrease",
                modifier = Modifier
                    .padding(10.dp)
                    .size(12.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(Modifier.width(10.dp))
        Text(
            text = count.toString(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primaryFixed,
            fontSize = 19.sp
        )
        Spacer(Modifier.width(10.dp))

        CircularPrimaryButton(
            onClick = onIncreaseClick
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_plus_icon),
                contentDescription = "Increase",
                modifier = Modifier
                    .padding(10.dp)
                    .size(12.dp),
                tint = Color.White,
            )
        }
    }
}