package com.foodsaver.app.presentation.featurePaymentMethod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.congratulations
import foodsaver.composeapp.generated.resources.successful_maked_payment
import foodsaver.composeapp.generated.resources.successful_payment_image
import foodsaver.composeapp.generated.resources.track_order
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentSuccessfulScreenRoot(
    navController: NavController
) {

    PaymentSuccessfulScreen(navController)
}

@Composable
private fun PaymentSuccessfulScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodSaverTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.successful_payment_image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = stringResource(Res.string.congratulations),
                color = FoodSaverTheme.colorScheme.backgroundContrast,
                style = FoodSaverTheme.typography.headerBold
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.successful_maked_payment),
                color = FoodSaverTheme.colorScheme.onBackgroundThin.copy(.6f),
                style = FoodSaverTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        PrimaryButton(
            text = stringResource(Res.string.track_order),
            onClick = {
                // TODO
                navController.navigate(Route.MainGraph.HomeScreen) {
                    popUpTo(Route.PaymentMethodGraph.PaymentSuccessfulScreen) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
                .padding(WindowInsets.navigationBars.asPaddingValues())
        )
    }
}