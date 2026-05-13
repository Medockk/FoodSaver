package com.foodsaver.app.presentation.featurePaymentMethod.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.check_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PaymentMethodTypes(
    types: List<PaymentMethodTypesModel>,
    onTypeClick: (index: Int, type: PaymentMethodTypesModel) -> Unit,
    isTypeSelected: (index: Int, type: PaymentMethodTypesModel) -> Boolean,
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(types) { index, type ->

            val isSelected = isTypeSelected(index, type)

            Box {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onTypeClick(index, type)
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .sizeIn(minWidth = 85.dp, minHeight = 75.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(FoodSaverTheme.colorScheme.placeholderBackground)
                            .then(
                                if (isSelected) {
                                    Modifier.border(
                                        width = 2.dp,
                                        color = FoodSaverTheme.colorScheme.primary,
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                                    )
                                }
                                else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImageWithShimmerLoading(
                            model = type.iconUri,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .sizeIn(minWidth = 26.dp, minHeight = 26.dp)
                        )
                    }

                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = type.name,
                        color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
                        style = FoodSaverTheme.typography.bodySmall
                    )
                }

                AnimatedVisibility (
                    visible = isSelected,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 5.dp, y = -(5).dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(FoodSaverTheme.colorScheme.primary, CircleShape)
                            .border(
                                width = 2.dp,
                                color = FoodSaverTheme.colorScheme.background,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.check_icon),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(10.dp, 6.dp)
                        )
                    }
                }
            }
        }
    }
}