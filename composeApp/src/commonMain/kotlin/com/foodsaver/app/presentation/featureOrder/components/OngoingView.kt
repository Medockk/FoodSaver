package com.foodsaver.app.presentation.featureOrder.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.common.button.InversedPrimaryButton
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.featureOrder.domain.model.OrderModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.cancel
import foodsaver.composeapp.generated.resources.order_items
import foodsaver.composeapp.generated.resources.track_order
import org.jetbrains.compose.resources.stringResource

@Composable
fun OngoingView(
    orders: List<OrderModel>,
    onOrderClick: (order: OrderModel) -> Unit,
    onTrackClick: (order: OrderModel) -> Unit,
    onCancelClick: (order: OrderModel) -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        modifier = modifier,
    ) {

        items(orders) { order ->
            Box(Modifier.clickable {
                onOrderClick(order)
            }) {
                Column {
                    Text(
                        text = order.type.name,
                        color = FoodSaverTheme.colorScheme.onBackground,
                        style = FoodSaverTheme.typography.bodySmall
                    )

                    Spacer(Modifier.height(15.dp))

                    HorizontalDivider(color = FoodSaverTheme.colorScheme.placeholderHint.copy(.2f))

                    Spacer(Modifier.height(15.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImageWithShimmerLoading(
                            model = order.restaurantImageUri,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(Modifier.width(15.dp))

                        Column {
                            Text(
                                text = order.restaurantName,
                                color = FoodSaverTheme.colorScheme.onBackground,
                                style = FoodSaverTheme.typography.bodyRegularBold
                            )
                            Spacer(Modifier.height(10.dp))
                            Row {
                                Text(
                                    text = order.restaurantName,
                                    color = FoodSaverTheme.colorScheme.onBackground,
                                    style = FoodSaverTheme.typography.bodyRegularBold
                                )
                                Spacer(Modifier.width(15.dp))
                                Spacer(
                                    Modifier.size(1.dp, 15.dp)
                                        .background(
                                            FoodSaverTheme.colorScheme.placeholderHint.copy(
                                                .2f
                                            )
                                        )
                                )
                                Spacer(Modifier.width(15.dp))

                                Text(
                                    text = "${order.items.size} " + stringResource(Res.string.order_items),
                                    color = FoodSaverTheme.colorScheme.onBackgroundThin,
                                    style = FoodSaverTheme.typography.bodySmall
                                )
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = "#" + order.trackNumber,
                            style = FoodSaverTheme.typography.bodySmall,
                            color = FoodSaverTheme.colorScheme.onBackgroundThin,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .offset(y = -(5).dp)
                        )
                    }

                    Spacer(Modifier.height(25.dp))

                    Row {
                        PrimaryButton(
                            text = stringResource(Res.string.track_order),
                            onClick = {
                                onTrackClick(order)
                            },
                            minHeight = 40.dp,
                            modifier = Modifier
                                .weight(1f)
                        )
                        Spacer(Modifier.weight(.2f))
                        InversedPrimaryButton(
                            text = stringResource(Res.string.cancel),
                            onClick = {
                                onCancelClick(order)
                            },
                            minHeight = 40.dp,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}