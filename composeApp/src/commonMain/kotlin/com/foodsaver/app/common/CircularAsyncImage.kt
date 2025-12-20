package com.foodsaver.app.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

@Composable
fun CircularAsyncImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    maxSize: Dp = 100.dp,
) {

    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
            .sizeIn(maxWidth = maxSize, maxHeight = maxSize)
            .aspectRatio(1f)
            .clip(CircleShape),
        loading = {
            Box(Modifier.fillMaxSize().shimmerEffect())
        },
        success = {
            SubcomposeAsyncImageContent()
        },
        contentScale = ContentScale.Crop
    )
}