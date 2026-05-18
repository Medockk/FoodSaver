package com.foodsaver.app.presentation.featureProfile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.coreAddress.domain.model.AddressModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.address_home_icon
import foodsaver.composeapp.generated.resources.delete_icon
import foodsaver.composeapp.generated.resources.edit_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ProfileAddressCard(
    address: AddressModel,
    onEditAddressClick: () -> Unit,
    onDeleteAddressClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(FoodSaverTheme.colorScheme.backgroundSecondary)
            .padding(16.dp)
    ) {
        Row {
            PrimaryFabButton(
                onClick = {  },
                background = FoodSaverTheme.colorScheme.background,
                size = 40.dp,
                innerPadding = 10.dp
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.address_home_icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = address.name.uppercase(),
                        style = FoodSaverTheme.typography.bodySmall,
                        color = FoodSaverTheme.colorScheme.onBackgroundSecondary
                    )

                    Spacer(Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(5.dp)
                            .clickable {
                                onEditAddressClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.edit_icon),
                            contentDescription = null,
                            tint = FoodSaverTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(20.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(5.dp)
                            .clickable {
                                onDeleteAddressClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.delete_icon),
                            contentDescription = null,
                            tint = FoodSaverTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    text = address.fullAddress,
                    color = FoodSaverTheme.colorScheme.onBackgroundSecondary.copy(.5f),
                    style = FoodSaverTheme.typography.bodySmall
                )
            }
        }
    }
}