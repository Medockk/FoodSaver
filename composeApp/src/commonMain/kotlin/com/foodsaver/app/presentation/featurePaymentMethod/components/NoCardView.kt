package com.foodsaver.app.presentation.featurePaymentMethod.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_icon
import foodsaver.composeapp.generated.resources.add_new
import foodsaver.composeapp.generated.resources.default_empty_card
import foodsaver.composeapp.generated.resources.no_card_added
import foodsaver.composeapp.generated.resources.you_can_add_card
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun NoCardView(
    type: PaymentMethodTypesModel,
    onAddNewClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(FoodSaverTheme.colorScheme.placeholderBackground)
                .padding(all = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(15.dp))
                Image(
                    painter = painterResource(Res.drawable.default_empty_card),
                    contentDescription = null,
                    modifier = Modifier
                        .sizeIn(165.dp, 105.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(25.dp))

                Text(
                    text = stringResource(Res.string.no_card_added, type.name),
                    color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
                    style = FoodSaverTheme.typography.headerRegularBold
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = stringResource(Res.string.you_can_add_card, type.name),
                    color = FoodSaverTheme.colorScheme.onBackgroundSubtitle.copy(0.7f),
                    style = FoodSaverTheme.typography.bodyRegular,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(FoodSaverTheme.colorScheme.background)
                .border(
                    width = 2.dp,
                    color = FoodSaverTheme.colorScheme.placeholderBackground,
                    shape = RoundedCornerShape(10.dp)
                ).clickable(onClick = onAddNewClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(Modifier.padding(vertical = 20.dp)) {
                Icon(
                    imageVector = vectorResource(Res.drawable.add_icon),
                    contentDescription = null,
                    tint = FoodSaverTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(10.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(Res.string.add_new),
                    style = FoodSaverTheme.typography.bodyRegularBold,
                    color = FoodSaverTheme.colorScheme.primary
                )
            }
        }
    }
}