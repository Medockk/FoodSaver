package com.foodsaver.app.presentation.FeatureHome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.foodsaver.app.domain.model.OfferModel
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.buy
import org.jetbrains.compose.resources.stringResource

@Composable
fun OfferCard(
    offer: OfferModel,
    onClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(0.75f),
                        MaterialTheme.colorScheme.primaryContainer,
                    )
                ),
                shape = RoundedCornerShape(10.dp)
            ),
    ) {
        Box(Modifier.fillMaxWidth()) {
            offer.imageUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = offer.title,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(0.7f)
                        .align(Alignment.BottomEnd),
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, top = 30.dp, bottom = 20.dp)
                    .fillMaxWidth(0.7f)
                    .align(Alignment.CenterStart),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = offer.title,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 3,
                    autoSize = TextAutoSize.StepBased(12.sp, 16.sp)
                )
                Spacer(Modifier.height(2.dp))
                offer.description?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 8.sp
                    )
                }

                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable {
                            onClick(offer.productId)
                        }
                ) {
                    Text(
                        text = stringResource(Res.string.buy),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}