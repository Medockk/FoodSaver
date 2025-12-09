@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.FeatureMain.Home.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.CircularPrimaryButton
import com.foodsaver.app.common.PrimaryButton
import com.foodsaver.app.domain.model.ProductModel
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_expires_icon
import foodsaver.composeapp.generated.resources.ic_minus_icon
import foodsaver.composeapp.generated.resources.ic_plus_icon
import org.jetbrains.compose.resources.painterResource

context(scope: AnimatedContentScope)
@Composable
fun SharedTransitionScope.ProductCard(
    product: ProductModel,
    isInCart: Boolean,
    onProductClick: (productId: String) -> Unit,
    onAddProductClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val plusIconRotationValue = 180f
    val centerRotateValue = plusIconRotationValue / 2
    val minusIconRotationValue = 0f

    val animatedIconRotation by animateFloatAsState(
        targetValue = if (isInCart) minusIconRotationValue
        else plusIconRotationValue,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing)
    )

    Card(
        //elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier
            .padding(5.dp)
            .dropShadow(
                shape = RoundedCornerShape(10.dp),
                shadow = Shadow(radius = 8.dp, color = Color(0x1F000000))
            ),
        onClick = {
            onProductClick(product.productId)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(Modifier.padding(10.dp)) {
            Row {
                Icon(
                    painter = painterResource(Res.drawable.ic_expires_icon),
                    contentDescription = "expires at",
                    tint = MaterialTheme.colorScheme.surfaceDim
                )
                Text(
                    text = product.expiresAt,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_expiresAt_${product.productId}"),
                            animatedVisibilityScope = scope,
                            boundsTransform = { _, _ ->
                                tween()
                            }
                        ),
                    color = MaterialTheme.colorScheme.surfaceDim
                )
            }
        }
        Spacer(Modifier.height(5.dp))

        Text(
            text = product.name,
            modifier = Modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState("product_name_${product.productId}"),
                    animatedVisibilityScope = scope,
                    boundsTransform = { _, _ ->
                        tween()
                    }
                ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = product.description,
            modifier = Modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState("product_desc_${product.productId}"),
                    animatedVisibilityScope = scope,
                    boundsTransform = { _, _ ->
                        tween()
                    }
                ),
            color = MaterialTheme.colorScheme.secondaryFixedDim
        )

        Spacer(Modifier.height(15.dp))
        Row {
            Text(
                text = product.cost.toString(),
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("product_cost_${product.productId}"),
                        animatedVisibilityScope = scope
                    ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(40.dp))

            CircularPrimaryButton(
                content = {
                    Icon(
                        painter = painterResource(
                            resource = if (animatedIconRotation in centerRotateValue..plusIconRotationValue) {
                                Res.drawable.ic_plus_icon
                            } else {
                                Res.drawable.ic_minus_icon
                            }
                        ),
                        modifier = Modifier
                            .size(15.dp)
                            .graphicsLayer {
                                this.rotationZ = animatedIconRotation
                                this.alpha = if (animatedIconRotation in 45f..centerRotateValue || animatedIconRotation in centerRotateValue..135f) 0.8f
                                else 1f
                            },
                        contentDescription = null,
                        tint = Color.White,
                    )
                },
                onClick = {
                    onAddProductClick(product.productId)
                },
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("product_btn_${product.productId}"),
                        animatedVisibilityScope = scope,
                        boundsTransform = { _, _ ->
                            spring(Spring.DampingRatioLowBouncy)
                        }
                    )
            )
        }

    }
}