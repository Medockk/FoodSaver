package com.foodsaver.app.common.asyncImage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.ui.FoodSaverTheme
import com.kmpalette.rememberDominantColorState
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.warning_error_icon
import io.kamel.core.Resource
import io.kamel.core.config.ResourceConfig
import io.kamel.core.loadImageBitmapResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.kamel.image.config.LocalKamelConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AsyncImageWithShimmerLoading(
    model: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shimmerDurationMillis: Int = 3000
) {
    Box(modifier) {
        KamelImage(
            resource = { asyncPainterResource(model ?: "") },
            contentDescription = null,
            onLoading = {
                Box(Modifier.matchParentSize().shimmerEffect(shimmerDurationMillis))
            },
            onFailure = {
                Box(Modifier.fillMaxSize().shimmerEffect(shimmerDurationMillis))
            },
            modifier = Modifier.matchParentSize(),
            contentScale = contentScale
        )
    }
}

@Composable
fun AsyncImageWithShimmerLoading(
    model: List<Any>?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onPageChange: ((index: Int) -> Unit)? = null,
    onColorGenerated: (Color) -> Unit = {},
    initialPagerSize: Int = 1,
    shimmerDurationMillis: Int = 3000
) {
    val pagerState = rememberPagerState { model?.size ?: initialPagerSize }
    val dominantColorState = rememberDominantColorState(
        defaultColor = FoodSaverTheme.colorScheme.background,
        defaultOnColor = FoodSaverTheme.colorScheme.onBackground
    )
    val kamelConfig = LocalKamelConfig.current
    val density = LocalDensity.current

    val currentUrl = model?.getOrNull(pagerState.currentPage)?.toString() ?: ""

    LaunchedEffect(currentUrl) {
        if (currentUrl.isNotEmpty()) {
            val config = createResourceConfig(currentUrl, density)
            kamelConfig.loadImageBitmapResource(currentUrl, config).collect { resource ->
                if (resource is Resource.Success) {
                    dominantColorState.updateFrom(resource.value)
                }
            }
        }
    }

    LaunchedEffect(dominantColorState.color) {
        if (dominantColorState.color != Color.Unspecified) {
            onColorGenerated(dominantColorState.color)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onPageChange?.invoke(pagerState.currentPage)
    }

    Box(modifier = modifier) {
        HorizontalPager(state = pagerState) { page ->
            val pageUrl = model?.getOrNull(page)?.toString() ?: ""

            KamelImage(
                resource = { asyncPainterResource(pageUrl) },
                contentDescription = null,
                onLoading = {
                    Box(Modifier.matchParentSize().shimmerEffect(shimmerDurationMillis))
                },
                onFailure = {
                    Box(Modifier.fillMaxSize().shimmerEffect(shimmerDurationMillis))
                },
                modifier = Modifier.matchParentSize(),
                contentScale = contentScale
            )
        }
    }
}

private fun createResourceConfig(url: String, density: Density) = object : ResourceConfig {
    override val requestData = HttpRequestBuilder().apply { url(url) }.build()
    override val coroutineContext = kotlin.coroutines.EmptyCoroutineContext
    override val density = density
    override val maxBitmapDecodeSize = IntSize.Zero
}