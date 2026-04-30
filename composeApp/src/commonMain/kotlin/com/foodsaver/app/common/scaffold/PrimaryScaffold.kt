package com.foodsaver.app.common.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.app_name
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.selected_favorite_icon
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PrimaryScaffoldPreview() {
    LocalFoodSaverThemeComposition {
        PrimaryScaffold(
            navigationButton = ActionButtonItem(
                icon = Res.drawable.back_icon,
                onClick = {}
            ),
            actionButton = {
                PrimaryFabButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.selected_favorite_icon),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            },
            title = Res.string.app_name,
            backgroundContent = {
                Box(
                    Modifier.fillMaxWidth().heightIn(300.dp)
                        .background(FoodSaverTheme.colorScheme.shimmerColor)
                )
            }
        ) { paddingValues ->
            Box(
                Modifier.fillMaxSize().padding(paddingValues)
                    .background(FoodSaverTheme.colorScheme.background)
            ) {
                Text("QAZxdfghjkil")
            }
        }
    }
}

@Composable
fun PrimaryScaffold(
    modifier: Modifier = Modifier,
    navigationButton: ActionButtonItem? = null,
    title: StringResource? = null,
    actionButton: ActionButtonItem? = null,
    backgroundContent: (@Composable () -> Unit)? = null,
    bottomBackgroundContent: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier,
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            Box(Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))) {
                backgroundContent?.invoke()

                Row(
                    modifier = Modifier
                        .padding(
                            start = 24.dp, end = 24.dp,
                            top = 50.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navigationButton?.let { navigationButton ->
                        PrimaryFabButton(
                            onClick = navigationButton.onClick,
                            background = navigationButton.backgroundColor
                        ) {
                            Icon(
                                imageVector = vectorResource(navigationButton.icon),
                                contentDescription = null,
                                tint = navigationButton.onBackgroundColor
                            )
                        }

                        Spacer(Modifier.width(15.dp))
                    }

                    title?.let { title ->
                        Text(
                            text = stringResource(title),
                            style = FoodSaverTheme.typography.bodyRegular,
                            color = FoodSaverTheme.colorScheme.onBackground
                        )

                        Spacer(Modifier.weight(1f))
                    }


                    actionButton?.let { actionButton ->
                        Spacer(Modifier.weight(1f))
                        PrimaryFabButton(
                            onClick = actionButton.onClick,
                            background = actionButton.backgroundColor
                        ) {
                            Icon(
                                imageVector = vectorResource(actionButton.icon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)
                ) {
                    bottomBackgroundContent?.invoke()
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .offset(y = -(25).dp)
        ) {
            val topPaddingValues = paddingValues.calculateTopPadding() + 25.dp
            content(PaddingValues(top = topPaddingValues))
        }
    }
}

@Composable
fun PrimaryScaffold(
    modifier: Modifier = Modifier,
    navigationButton: ActionButtonItem? = null,
    title: StringResource? = null,
    actionButton: (@Composable () -> Unit)? = null,
    backgroundContent: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
)
{

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier,
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            Box(Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))) {
                backgroundContent?.invoke()

                Row(
                    modifier = Modifier
                        .padding(
                            start = 24.dp, end = 24.dp,
                            top = 50.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navigationButton?.let { navigationButton ->
                        PrimaryFabButton(
                            onClick = navigationButton.onClick,
                            background = navigationButton.backgroundColor
                        ) {
                            Icon(
                                imageVector = vectorResource(navigationButton.icon),
                                contentDescription = null,
                                tint = navigationButton.onBackgroundColor
                            )
                        }

                        Spacer(Modifier.width(15.dp))
                    }

                    title?.let { title ->
                        Text(
                            text = stringResource(title),
                            style = FoodSaverTheme.typography.bodyRegular,
                            color = FoodSaverTheme.colorScheme.onBackground
                        )

                    }


                    Spacer(Modifier.weight(1f))
                    actionButton?.invoke()
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .offset(y = -(25).dp)
        ) {
            val topPaddingValues = paddingValues.calculateTopPadding() + 25.dp
            content(PaddingValues(top = topPaddingValues))
        }
    }
}

@Composable
fun PrimaryScaffold(
    modifier: Modifier = Modifier,
    navigationButton: ActionButtonItem? = null,
    title: StringResource? = null,
    actionButton: (@Composable () -> Unit)? = null,
    backgroundContent: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
)
{

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier,
        containerColor = FoodSaverTheme.colorScheme.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                bottomBar?.invoke()
            }
        },
        topBar = {
            Box(Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))) {
                backgroundContent?.invoke()

                Row(
                    modifier = Modifier
                        .padding(
                            start = 24.dp, end = 24.dp,
                            top = 50.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navigationButton?.let { navigationButton ->
                        PrimaryFabButton(
                            onClick = navigationButton.onClick,
                            background = navigationButton.backgroundColor
                        ) {
                            Icon(
                                imageVector = vectorResource(navigationButton.icon),
                                contentDescription = null,
                                tint = navigationButton.onBackgroundColor
                            )
                        }

                        Spacer(Modifier.width(15.dp))
                    }

                    title?.let { title ->
                        Text(
                            text = stringResource(title),
                            style = FoodSaverTheme.typography.bodyRegular,
                            color = FoodSaverTheme.colorScheme.onBackground
                        )

                    }


                    Spacer(Modifier.weight(1f))
                    actionButton?.invoke()
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .offset(y = -(25).dp)
        ) {
            val topPaddingValues = paddingValues.calculateTopPadding() + 25.dp
            content(PaddingValues(top = topPaddingValues))
        }
    }
}