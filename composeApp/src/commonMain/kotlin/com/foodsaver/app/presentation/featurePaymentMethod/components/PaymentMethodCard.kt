package com.foodsaver.app.presentation.featurePaymentMethod.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodCardModel
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.expand_icon
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PaymentMethodCard(
    card: PaymentMethodCardModel,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(FoodSaverTheme.colorScheme.placeholderBackground)
            .padding(horizontal = 20.dp, vertical = 15.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = card.type.name,
                    style = FoodSaverTheme.typography.bodyRegularBold,
                    color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
                )
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(FoodSaverTheme.colorScheme.onBackgroundSubtitle)
                            .padding(1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        card.type.iconUri?.let { iconUri ->
                            KamelImage(
                                resource = { asyncPainterResource(iconUri) },
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp, 20.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Spacer(Modifier.width(7.dp))

                    card.lastFourSymbols?.let { lastSymbols ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(
                                    color = FoodSaverTheme.colorScheme.onBackgroundSubtitle.copy(.5f)
                                )) {
                                    append("**** **** **** ")
                                }
                                withStyle(SpanStyle(
                                    color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
                                )) {
                                    append(lastSymbols)
                                }
                            },
                            style = FoodSaverTheme.typography.bodyRegular
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = onExpandClick
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.expand_icon),
                    contentDescription = null,
                    tint = FoodSaverTheme.colorScheme.onBackground
                )
            }
        }
    }
}