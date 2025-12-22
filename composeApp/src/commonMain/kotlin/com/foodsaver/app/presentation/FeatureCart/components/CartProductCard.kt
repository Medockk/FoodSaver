@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.FeatureCart.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.utils.ScreenAnimation

@Composable
fun SharedTransitionScope.CartProductCard(
    cartItem: CartItemModel,
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryFixed.copy(
                        0.75f
                    ),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .sizeIn(maxHeight = 25.dp, maxWidth = 25.dp)
                        .aspectRatio(1f)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(ScreenAnimation.Cart_ProductDetail.countAnim(cartItem.product.productId)),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween()
                            },
                            renderInOverlayDuringTransition = false
                        ),
                ) {
                    Text(
                        text = cartItem.quantity.toString(),
                        modifier = Modifier
                            .padding(5.dp),
                        color = Color.White
                    )
                }
            }
        ) {
            SubcomposeAsyncImage(
                model = cartItem.product.photoUrl,
                contentDescription = cartItem.product.title,
                modifier = Modifier
                    .size(100.dp, 80.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(ScreenAnimation.Cart_ProductDetail.imageAnim(cartItem.product.productId)),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween()
                        },
                        renderInOverlayDuringTransition = false
                    ),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = cartItem.product.title,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(ScreenAnimation.Cart_ProductDetail.nameAnim(cartItem.product.productId)),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween()
                        },
                        renderInOverlayDuringTransition = false
                    ),
            )
            Text(
                text = "${cartItem.product.unit}${cartItem.product.unitType.value}",
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(ScreenAnimation.Cart_ProductDetail.unitAnim(cartItem.product.productId)),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween()
                        },
                        renderInOverlayDuringTransition = false
                    ),
            )
        }

        Text(
            text = cartItem.product.costUnit,
        )
        Text(
            text = cartItem.product.cost.toString(),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState(ScreenAnimation.Cart_ProductDetail.costAnim(cartItem.product.productId)),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween()
                    },
                    renderInOverlayDuringTransition = false
                ),
        )
    }
}