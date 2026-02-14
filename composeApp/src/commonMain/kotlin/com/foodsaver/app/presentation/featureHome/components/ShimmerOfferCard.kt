package com.foodsaver.app.presentation.featureHome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.shimmerEffect

@Composable
fun ShimmerOfferCard(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .shimmerEffect(2000)
    ) {
        val backgroundColor = Color.LightGray.copy(0.7f)
        Column(
            modifier = Modifier
                .padding(start = 20.dp, top = 30.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp, 30.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(backgroundColor)
            )
            Spacer(Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .size(100.dp, 20.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(backgroundColor)
            )
            Box(
                modifier = Modifier
                    .size(70.dp, 20.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(backgroundColor)
            )
        }
    }
}