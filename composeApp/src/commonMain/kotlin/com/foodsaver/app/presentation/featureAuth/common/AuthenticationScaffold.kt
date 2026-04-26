package com.foodsaver.app.presentation.featureAuth.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.PrimaryFabButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.authentication_fiber
import foodsaver.composeapp.generated.resources.authentication_slices
import foodsaver.composeapp.generated.resources.back_icon
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AuthenticationScaffold(
    title: StringResource,
    subtitle: StringResource,
    modifier: Modifier = Modifier,
    leftIconTint: Color = FoodSaverTheme.colorScheme.authenticationIconsTint,
    onBackButtonClick: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState? = null,
    content: @Composable () -> Unit,
) {

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            Box(Modifier.imePadding()) {
                snackbarHostState?.let {
                    SnackbarHost(snackbarHostState)
                }
            }
        },
        containerColor = FoodSaverTheme.colorScheme.backgroundContrast,
        contentWindowInsets = WindowInsets.navigationBars
    ) { paddingValues ->
        Box {
            Image(
                painterResource(Res.drawable.authentication_slices),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.TopStart),
                colorFilter = ColorFilter.tint(FoodSaverTheme.colorScheme.authenticationIconsTint)
            )
            Image(
                painterResource(Res.drawable.authentication_fiber),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp, 300.dp)
                    .align(Alignment.TopEnd),
                colorFilter = ColorFilter.tint(leftIconTint)
            )

            if (onBackButtonClick != null) {

                PrimaryFabButton(
                    onClick = onBackButtonClick,
                    modifier = Modifier
                        .padding(top = 50.dp, start = 24.dp)
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.back_icon),
                        null,
                        modifier = Modifier
                            .size(5.dp, 10.dp),
                        tint = FoodSaverTheme.colorScheme.onFabBackground
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(100.dp))

                Text(
                    text = stringResource(title),
                    style = FoodSaverTheme.typography.headerBold,
                    color = FoodSaverTheme.colorScheme.onBackgroundContrast
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = stringResource(subtitle),
                    style = FoodSaverTheme.typography.bodyRegular,
                    color = FoodSaverTheme.colorScheme.onBackgroundContrast,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(50.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            color = FoodSaverTheme.colorScheme.background,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                        .padding(24.dp)
                ) {
                    content()
                }
            }
        }
    }
}