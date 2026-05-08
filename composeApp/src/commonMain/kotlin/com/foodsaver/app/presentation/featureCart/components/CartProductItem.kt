package com.foodsaver.app.presentation.featureCart.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.ProductCounter
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.close_icon
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CartProductItemPreview() {

    var isRemoved by remember { mutableStateOf(false) }

    val state = CartProductItemState(
        productName = "Qewewyyfkfdkdgfdqfaggggggggge",
        productPrice = 120.00,
        productSize = "14''",
        productImageUri = listOf(""),
        productCount = 2,
        onIncreaseClick = { TODO() },
        onDecreaseClick = { TODO() },
        onRemoveClick = { TODO() },
        isProductEditing = isRemoved
    )
    LocalFoodSaverThemeComposition {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FoodSaverTheme.colorScheme.backgroundContrast)
        ) {
            LazyColumn {
                items(10) {
                    CartProductItem(state, Modifier.fillMaxWidth()
                        .clickable {
                            isRemoved = !isRemoved
                        })
                }
            }
        }

    }
}

@Composable
fun CartProductItem(
    state: CartProductItemState,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        AsyncImageWithShimmerLoading(
            model = state.productImageUri,
            modifier = Modifier
                .size(135.dp, 120.dp)
                .padding(1.dp)
                .clip(CartImageShape())
        )

        Spacer(Modifier.width(20.dp))

        Column {
            Row {
                Text(
                    text = state.productName,
                    color = FoodSaverTheme.colorScheme.onBackgroundContrast,
                    modifier = Modifier
                        .animateContentSize(tween())
                        .weight(1f),
                    style = FoodSaverTheme.typography.bodyRegular
                )

                AnimatedVisibility(
                    visible = state.isProductEditing,
                    enter = fadeIn(tween()),
                    exit = fadeOut(tween())
                ) {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(FoodSaverTheme.colorScheme.deleteColor)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(),
                                onClick = state.onDecreaseClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = vectorResource(Res.drawable.close_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(10.dp),
                            tint = FoodSaverTheme.colorScheme.onBackgroundContrast
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = state.productPrice.toString(),
                color = FoodSaverTheme.colorScheme.onBackgroundContrast,
                style = FoodSaverTheme.typography.bodyBold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = state.productSize,
                    color = FoodSaverTheme.colorScheme.onBackgroundThin,
                    style = FoodSaverTheme.typography.bodyRegular
                )
                Spacer(Modifier.weight(1f))
                ProductCounter(
                    productCount = state.productCount.toInt(),
                    onIncreaseClick = state.onIncreaseClick,
                    onDecreaseClick = state.onDecreaseClick
                )
            }
        }
    }
}