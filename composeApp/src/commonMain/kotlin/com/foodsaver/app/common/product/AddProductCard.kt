package com.foodsaver.app.common.product

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.coreModel.model.ExpiresDateType
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreModel.model.ProductUnitType
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.remove_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AddProductCard(
    product: ProductModel,
    isProductInCart: Boolean,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onProductClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val priceTextStyle = FoodSaverTheme.typography.bodyRegularBold
    val priceTextColor = FoodSaverTheme.colorScheme.onBackground

    val plusIconRotation by animateFloatAsState(
        targetValue = if (isProductInCart) 0f
        else 90f,
        animationSpec = tween()
    )

    Box(
        modifier = modifier
            .dropShadow(
                shape = RoundedCornerShape(25.dp),
                shadow = Shadow(
                    radius = 12.dp,
                    color = FoodSaverTheme.colorScheme.primaryShadowColor,
                    alpha = .15f,
                    offset = DpOffset(x = 12.dp, 12.dp)
                )
            )
            .clip(RoundedCornerShape(25.dp))
            .background(FoodSaverTheme.colorScheme.background)
            .clickable(
                indication = ripple(),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onProductClick
            )
    ) {

        Column(
            modifier = Modifier
                .padding(vertical = 15.dp, horizontal = 12.dp)
        ) {

            AsyncImageWithShimmerLoading(
                model = product.imageUris,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(min = 75.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(5.dp))
            Text(
                text = product.name,
                style = FoodSaverTheme.typography.bodyRegularBold,
                color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = product.description,
                style = FoodSaverTheme.typography.bodySmall,
                color = FoodSaverTheme.colorScheme.onBackgroundThin,
                maxLines = 2
            )
            Spacer(Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.currency + " ",
                    style = priceTextStyle,
                    color = priceTextColor
                )
                Text(
                    text = product.price.toString(),
                    color = priceTextColor,
                    style = priceTextStyle
                )

                Spacer(Modifier.weight(1f))

                PrimaryFabButton(
                    onClick = {
                        if (isProductInCart) {
                            onRemoveClick()
                        } else {
                            onAddClick()
                        }
                    },
                    innerPadding = 8.dp,
                    size = 30.dp,
                    background = FoodSaverTheme.colorScheme.primary
                ) {

                    Icon(
                        imageVector = vectorResource(Res.drawable.remove_icon),
                        contentDescription = null,
                        tint = Color.White
                    )

                    Icon(
                        imageVector = vectorResource(Res.drawable.remove_icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(12.dp)
                            .graphicsLayer {
                                rotationZ = plusIconRotation
                            }
                    )
                }
            }
        }
    }
}