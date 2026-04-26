package com.foodsaver.app.presentation.featureOnBoarding.components

import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.onboarding_button_next
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class OnBoardingItem(
    val image: DrawableResource,
    val title: StringResource,
    val subtitle: StringResource,
    val buttonText: StringResource = Res.string.onboarding_button_next
)
