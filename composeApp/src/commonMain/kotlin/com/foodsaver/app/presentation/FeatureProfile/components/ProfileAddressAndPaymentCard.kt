package com.foodsaver.app.presentation.FeatureProfile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.foodsaver.app.domain.model.AddressModel
import com.foodsaver.app.model.PaymentCardModel
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_mastercard_icon
import foodsaver.composeapp.generated.resources.selected_address_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileAddressCard(
    addressModel: AddressModel,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Image(
                        painter = painterResource(Res.drawable.selected_address_icon),
                        contentDescription = addressModel.address,
                        modifier = Modifier
                            .padding(10.dp)
                            .matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.width(7.dp))

            Column {
                Text(
                    text = addressModel.name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = addressModel.address,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun ProfilePaymentCard(
    paymentCardModel: PaymentCardModel,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 30.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_mastercard_icon),
                contentDescription = paymentCardModel.cardNumber,
                modifier = Modifier
                    .size(32.dp, 25.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(7.dp))

            Text(
                text = paymentCardModel.cardSecretNumber,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}