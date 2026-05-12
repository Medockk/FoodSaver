package com.foodsaver.app.common.searchField

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.clear_search_icon
import foodsaver.composeapp.generated.resources.search_field_placeholder
import foodsaver.composeapp.generated.resources.search_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchTextFieldPreview() {
    var q by remember { mutableStateOf(TextFieldValue("bbnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn66")) }
    SearchTextField(
        state = SearchTextFieldState(
            q, { q = it },
            onSearch = {},
            suggestion = "Hello World!"
        )
    )
}

@Composable
fun SearchTextField(
    state: SearchTextFieldState,
    modifier: Modifier = Modifier,
) {

    val suggestionToDisplay = if (
        state.suggestion?.startsWith(
            prefix = state.query.text,
            ignoreCase = true
        ) == true && state.query.text.isNotEmpty()
    ) {
        state.suggestion.drop(state.query.text.length)
    } else {
        ""
    }

    BasicTextField(
        value = state.query,
        onValueChange = state.onQueryChange,
        enabled = state.enabled,
        modifier = modifier
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Tab || keyEvent.key == Key.DirectionRight) {
                    if (suggestionToDisplay.isNotEmpty()) {
                        state.onQueryChange(
                            TextFieldValue(
                                state.suggestion ?: "",
                                selection = TextRange(state.suggestion?.length ?: 0)
                            )
                        )
                        true
                    } else false
                } else false
            },
        textStyle = FoodSaverTheme.typography.bodySmall,
        cursorBrush = SolidColor(Color.Black),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (state.suggestion?.startsWith(state.query.text, ignoreCase = true) == true) {
                    state.onQueryChange(
                        TextFieldValue(
                            state.suggestion,
                            selection = TextRange(state.suggestion.length)
                        )
                    )
                }

                state.onSearch(state.query)
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(
                        color = FoodSaverTheme.colorScheme.searchFieldBackground,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        state.onSearch(state.query)
                    }
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.search_icon),
                        contentDescription = null,
                        tint = FoodSaverTheme.colorScheme.onBackgroundTertiary
                    )
                }

                Box(
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.query.text.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.search_field_placeholder),
                            color = FoodSaverTheme.colorScheme.placeholderHint,
                            style = FoodSaverTheme.typography.bodySmall
                        )
                    }

                    if (suggestionToDisplay.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = state.query.text,
                                color = Color.Transparent,
                                style = FoodSaverTheme.typography.bodySmall
                            )
                            // suggestion
                            Text(
                                text = suggestionToDisplay,
                                color = Color.Gray.copy(alpha = 0.5f),
                                style = FoodSaverTheme.typography.bodySmall
                            )
                        }
                    }

                    // user input
                    innerTextField()

                    if (state.query.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                state.onQueryChange(TextFieldValue())
                            },
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.clear_search_icon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            }
        }
    )

}