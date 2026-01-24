package com.foodsaver.app.presentation.FeatureHome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.shimmerEffect

@Composable
fun ShimmerProductCard(modifier: Modifier = Modifier) {

    val backgroundColor = Color.LightGray.copy(0.7f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .shimmerEffect(2000)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(backgroundColor)
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(30.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(backgroundColor)
            )
            Spacer(Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(backgroundColor)
            )
            Spacer(Modifier.height(30.dp))
        }
    }
}