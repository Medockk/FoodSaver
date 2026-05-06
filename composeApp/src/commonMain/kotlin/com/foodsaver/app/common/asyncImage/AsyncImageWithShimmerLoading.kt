package com.foodsaver.app.common.asyncImage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.foodsaver.app.common.shimmerEffect

@Composable
fun AsyncImageWithShimmerLoading(
    model: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
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
            contentScale = contentScale,
            onState = { currentState ->
                state = currentState
            }
        )
    }
}

@Composable
fun AsyncImageWithShimmerLoading(
    model: List<Any>?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shimmerDurationMillis: Int = 3000
) {

    var state: AsyncImagePainter.State by retain {
        mutableStateOf(AsyncImagePainter.State.Empty)
    }
    val pagerState = rememberPagerState { model?.size ?: 0 }

    Box {
        if (state !is AsyncImagePainter.State.Success) {
            Box(modifier.matchParentSize().shimmerEffect(shimmerDurationMillis))
        }

        HorizontalPager(
            state = pagerState
        ) { page ->
            AsyncImage(
                model = model?.getOrNull(page),
                contentDescription = null,
                modifier = modifier
                    .matchParentSize(),
                contentScale = contentScale,
                onState = { currentState ->
                    state = currentState
                }
            )
        }
    }
}