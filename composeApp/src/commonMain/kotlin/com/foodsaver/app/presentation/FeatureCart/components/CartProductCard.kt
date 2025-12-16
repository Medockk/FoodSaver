package com.foodsaver.app.presentation.FeatureCart.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.foodsaver.app.domain.model.ProductModel

@Composable
fun CartProductCard(
    product: ProductModel,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryFixed,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = product.count.toString(),
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }
            }
        ) {
            SubcomposeAsyncImage(
                model = product.photoUrl,
                contentDescription = product.title,
                modifier = Modifier
                    .size(100.dp, 80.dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.title,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${product.unit}${product.unitType.value}",
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }

        Text(
            text = product.costUnit
        )
        Text(
            text = product.cost.toString(),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}