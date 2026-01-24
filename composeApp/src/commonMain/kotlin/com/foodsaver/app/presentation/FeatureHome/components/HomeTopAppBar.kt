@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.FeatureHome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.coreModel.model.AddressModel
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_burger_icon
import foodsaver.composeapp.generated.resources.ic_cart_icon
import foodsaver.composeapp.generated.resources.ic_location_icon
import foodsaver.composeapp.generated.resources.poppins_black
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeTopAppBar(
    currentAddress: AddressModel?,
    cartProductQuantity: Int,
    onBurgerClick: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            if (currentAddress != null) {
                Text(
                    text = currentAddress.address,
                    color = MaterialTheme.colorScheme.secondaryFixedDim,
                    fontSize = 12.sp
                )
            } else {
                Box(Modifier.size(100.dp, 15.dp).shimmerEffect())
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBurgerClick
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_burger_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(15.dp),
                        tint = Color.Unspecified
                    )
                }
                Spacer(Modifier.width(30.dp))
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_location_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        },
        actions = {
            BadgedBox(
                badge = {
                    if (cartProductQuantity != 0) {
                        Badge(
                            modifier = Modifier
                                .size(15.dp)
                                .align(Alignment.TopEnd)
                                .background(MaterialTheme.colorScheme.background, CircleShape)
                                .padding(0.5.dp)
                                .background(MaterialTheme.colorScheme.error, CircleShape),
                            containerColor = Color.Unspecified
                        ) {
                            Text(
                                text = cartProductQuantity.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 9.sp,
                                fontFamily = FontFamily(Font(Res.font.poppins_black))
                            )
                        }
                    }
                }
            ) {
                IconButton(
                    onClick = onCartClick
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_cart_icon),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        }
    )
}