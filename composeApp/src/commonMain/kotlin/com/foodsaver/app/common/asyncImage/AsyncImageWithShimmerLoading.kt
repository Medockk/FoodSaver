package com.foodsaver.app.common.asyncImage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.foodsaver.app.common.shimmerEffect

@Composable
fun AsyncImageWithShimmerLoading(
    model: Any?,
    modifier: Modifier = Modifier,
    shimmerDurationMillis: Int = 3000
) {

    var state: AsyncImagePainter.State by retain {
        mutableStateOf(AsyncImagePainter.State.Empty)
    }

    Box(modifier) {
        if (state !is AsyncImagePainter.State.Success) {
            Box(Modifier.matchParentSize().shimmerEffect(shimmerDurationMillis))
        }

        AsyncImage(
            model = model,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize(),
            onState = { currentState ->
                state = currentState
            }
        )
    }
}