@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.FeatureHome.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.foodsaver.app.common.CircularPrimaryButton
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.model.ExpiresDateType
import com.foodsaver.app.model.ProductModel
import com.foodsaver.app.utils.ScreenAnimation
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.days
import foodsaver.composeapp.generated.resources.hours
import foodsaver.composeapp.generated.resources.ic_expires_icon
import foodsaver.composeapp.generated.resources.ic_minus_icon
import foodsaver.composeapp.generated.resources.ic_plus_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row {
                Icon(
                    painter = painterResource(Res.drawable.ic_expires_icon),
                    contentDescription = "expires at",
                    tint = MaterialTheme.colorScheme.surfaceDim
                )

                val dateType = when (product.expiresDateType) {
                    ExpiresDateType.DAYS -> stringResource(Res.string.days)
                    ExpiresDateType.HOURS -> stringResource(Res.string.hours)
                }
                Text(
                    text = "${product.expiresAt} $dateType",
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(
                                ScreenAnimation.Home_ProductDetail.expiresAtAnim(
                                    product.productId
                                )
                            ),
                            animatedVisibilityScope = scope,
                            boundsTransform = { _, _ ->
                                tween()
                            }
                        ),
                    color = MaterialTheme.colorScheme.surfaceDim
                )
            }

            Spacer(Modifier.height(10.dp))

            SubcomposeAsyncImage(
                model = product.photoUrl,
                contentDescription = product.title,
                contentScale = ContentScale.FillBounds,
                clipToBounds = true,
                modifier = Modifier
                    .heightIn(max = 80.dp)
                    .fillMaxHeight()
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            ScreenAnimation.Home_ProductDetail.imageAnim(
                                product.productId
                            )
                        ),
                        animatedVisibilityScope = scope,
                        boundsTransform = { _, _ ->
                            tween()
                        }
                    ),
                loading = {
                    this@Column.AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween()),
                        exit = fadeOut(tween())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(7.dp))
                                .shimmerEffect()
                        )
                    }
                },
                success = {
                    SubcomposeAsyncImageContent()
                }
            )

            Spacer(Modifier.height(13.dp))

            Text(
                text = product.title,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            ScreenAnimation.Home_ProductDetail.nameAnim(
                                product.productId
                            )
                        ),
                        animatedVisibilityScope = scope,
                        boundsTransform = { _, _ ->
                            tween(450, easing = LinearEasing)
                        },
                        renderInOverlayDuringTransition = true
                    ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                autoSize = TextAutoSize.StepBased(maxFontSize = 20.sp, minFontSize = 14.sp),
                maxLines = 2
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = "${product.unit} ${product.unitType.value}",
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            ScreenAnimation.Home_ProductDetail.unitAnim(
                                product.productId
                            )
                        ),
                        animatedVisibilityScope = scope,
                        boundsTransform = { _, _ ->
                            tween()
                        }
                    ),
                color = MaterialTheme.colorScheme.secondaryFixedDim,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )

            Spacer(Modifier.height(15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${product.costUnit} ${product.cost.toInt()}",
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(
                                ScreenAnimation.Home_ProductDetail.costAnim(
                                    product.productId
                                )
                            ),
                            animatedVisibilityScope = scope,
                            boundsTransform = { _, _ ->
                                tween()
                            }
                        ),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.width(5.dp))
                if (product.oldCost != null) {
                    Text(
                        text = "${product.costUnit} ${product.oldCost!!.toInt()}",
                        color = MaterialTheme.colorScheme.primary.copy(0.5f),
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.LineThrough,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 10.sp,
                            maxFontSize = 18.sp
                        )
                    )
                }
                Spacer(Modifier.weight(1f))

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
                                    this.alpha =
                                        if (animatedIconRotation in 45f..centerRotateValue || animatedIconRotation in centerRotateValue..135f) 0.8f
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
                            sharedContentState = rememberSharedContentState(
                                ScreenAnimation.Home_ProductDetail.buttonAnim(
                                    product.productId
                                )
                            ),
                            animatedVisibilityScope = scope,
                            boundsTransform = { _, _ ->
                                tween()
                            },
                            renderInOverlayDuringTransition = true
                        )
                )
            }
        }
    }
}