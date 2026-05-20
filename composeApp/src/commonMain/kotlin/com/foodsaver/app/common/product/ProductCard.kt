package com.foodsaver.app.common.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.image.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.featureSearch.domain.model.ProductCardModel
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun ProductCard(
    state: ProductCardModel,
    onProductClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .clickable(
                indication = ripple(),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onProductClick
            )
            .padding(bottom = 40.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (40).dp)
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
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp, start = 12.dp, end = 12.dp, bottom = 15.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = state.name,
                    style = FoodSaverTheme.typography.bodyRegularBold,
                    color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = state.restaurantName,
                    style = FoodSaverTheme.typography.bodySmall,
                    color = FoodSaverTheme.colorScheme.onBackgroundThin
                )
            }
        }

        AsyncImageWithShimmerLoading(
            model = state.imageUri,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(min = 80.dp)
                .clip(RoundedCornerShape(15.dp))
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Fit
        )
    }

//    Box(
//        modifier = modifier
//            .dropShadow(
//                shape = RoundedCornerShape(25.dp),
//                shadow = Shadow(
//                    radius = 12.dp,
//                    color = FoodSaverTheme.colorScheme.primaryShadowColor,
//                    alpha = .15f,
//                    offset = DpOffset(x = 12.dp, 12.dp)
//                )
//            )
//            .clip(RoundedCornerShape(25.dp))
//            .background(FoodSaverTheme.colorScheme.background)
//            .clickable(
//                indication = ripple(),
//                interactionSource = remember { MutableInteractionSource() },
//                onClick = onProductClick
//            )
//    ) {
//
//        Column(
//            modifier = Modifier
//                .padding(vertical = 15.dp, horizontal = 12.dp)
//        ) {
//
//            AsyncImageWithShimmerLoading(
//                model = product.imageUris,
//                modifier = Modifier
//                    .fillMaxWidth(0.9f)
//                    .heightIn(min = 75.dp)
//                    .clip(RoundedCornerShape(15.dp))
//                    .align(Alignment.CenterHorizontally)
//            )
//
//            Spacer(Modifier.height(5.dp))
//            Text(
//                text = product.name,
//                style = FoodSaverTheme.typography.bodyRegularBold,
//                color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
//            )
//            Spacer(Modifier.height(5.dp))
//            Text(
//                text = product.description,
//                style = FoodSaverTheme.typography.bodySmall,
//                color = FoodSaverTheme.colorScheme.onBackgroundThin,
//                maxLines = 2
//            )
//            Spacer(Modifier.height(5.dp))
//        }
//    }
}