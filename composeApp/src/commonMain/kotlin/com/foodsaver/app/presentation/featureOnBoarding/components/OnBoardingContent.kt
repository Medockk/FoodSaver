package com.foodsaver.app.presentation.featureOnBoarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.ui.FoodSaverTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnBoardingContent(
    item: OnBoardingItem,
    itemCount: Int,
    currentItem: Int,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Image(
            painter = painterResource(item.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(300.dp),
            contentScale = ContentScale.FillBounds

        )

        Spacer(Modifier.height(60.dp))

        Text(
            text = stringResource(item.title),
            style = FoodSaverTheme.typography.headerMedium,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(item.subtitle),
            style = FoodSaverTheme.typography.bodyRegular,
            color = FoodSaverTheme.colorScheme.onBackgroundThin,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(30.dp))

        OnBoardingIndicator(itemCount, currentItem, Modifier.fillMaxWidth())

        Spacer(Modifier.height(70.dp))

        PrimaryButton(
            text = item.buttonText,
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))
    }
}