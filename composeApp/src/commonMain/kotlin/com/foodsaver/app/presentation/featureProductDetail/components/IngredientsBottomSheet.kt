@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.featureProductDetail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.classify_ingredients_by_categories
import foodsaver.composeapp.generated.resources.ic_back_icon
import foodsaver.composeapp.generated.resources.ingredients
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES
)
@Composable
private fun IngredientsBottomSheetPreview() {

    val scope = rememberCoroutineScope()

    val fakeAiResponse =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore " +
                "magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                "ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse " +
                "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat " +
                "cupidatat non proident, sunt in culpa qui officia deserunt " +
                "mollit anim id est laborum."

    val onClick: suspend () -> String = {
        delay(5000)
        fakeAiResponse
    }
    var aiResponse by retain { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        IngredientsBottomSheet(
            ingredients = listOf("Sugar, Flour, E250, Olive Oil"),
            aiResponse = aiResponse,
            isLoading = false,
            onGenerateAiResponseClick = {
                scope.launch {
                    aiResponse = onClick()
                }
            },
            onDismissRequest = {}
        )
    }
}

@Composable
fun IngredientsBottomSheet(
    ingredients: List<String>,
    aiResponse: String?,
    isLoading: Boolean,
    onGenerateAiResponseClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = FoodSaverTheme.colorScheme.background,
    shape: Shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
) {

    var isAiResponseExpand by retain(aiResponse) { mutableStateOf(aiResponse != null) }
    val animatedExpandButtonRotation by animateFloatAsState(
        targetValue = if (isAiResponseExpand) 270f
        else 180f,
        animationSpec = tween()
    )

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        containerColor = backgroundColor,
        shape = shape
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(bottom = 15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = stringResource(Res.string.ingredients),
                    color = FoodSaverTheme.colorScheme.onBackground,
                    fontSize = 20.sp
                )
            }

            Spacer(Modifier.height(15.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                text = ingredients.joinToString(),
                color = FoodSaverTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(Res.string.classify_ingredients_by_categories),
                    color = FoodSaverTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {
                                onGenerateAiResponseClick()
                            }
                        )
                )

                Spacer(Modifier.weight(1f))

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    aiResponse?.let { _ ->
                        Image(
                            painterResource(Res.drawable.ic_back_icon),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(24.dp)
                                .graphicsLayer {
                                    this.rotationZ = animatedExpandButtonRotation
                                }.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(),
                                    onClick = {
                                        isAiResponseExpand = !isAiResponseExpand
                                    }
                                ),
                            colorFilter = ColorFilter.tint(FoodSaverTheme.colorScheme.onBackground)
                        )
                    }
                }

            }

            Spacer(
                Modifier
                    .size(
                        if (isAiResponseExpand) 8.dp
                        else 5.dp
                    )
                    .animateContentSize()
            )

            Box(
                modifier = Modifier
                    .animateContentSize(alignment = Alignment.BottomStart)
            ) {
                this@Column.AnimatedVisibility(
                    visible = isAiResponseExpand && aiResponse != null,
                    enter = slideInVertically() + fadeIn(),
                    exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
                ) {
                    aiResponse?.let { aiResponse ->
                        Text(
                            text = aiResponse,
                            color = FoodSaverTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
                    }

                }
            }
        }
    }
}