package com.foodsaver.app.common.asyncImage

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.foodsaver.app.common.shimmerEffect

@Composable
fun AsyncImageWithShimmerLoading(
    model: Any?,
    modifier: Modifier = Modifier
) {

    var state: AsyncImagePainter.State by retain {
        mutableStateOf(AsyncImagePainter.State.Empty)
    }

    if (state !is AsyncImagePainter.State.Success) {
        Box(modifier.shimmerEffect(durationMillis = 7000))
    }

    AsyncImage(
        model = model,
        contentDescription = null,
        modifier = modifier,
        onState = { currentState ->
            state = currentState
        }
    )
}