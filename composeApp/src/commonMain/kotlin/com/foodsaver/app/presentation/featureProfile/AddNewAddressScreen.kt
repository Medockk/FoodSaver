package com.foodsaver.app.presentation.featureProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.common.textField.PrimaryTextFieldState
import com.foodsaver.app.common.textField.fieldItem.TextFieldItem
import com.foodsaver.app.common.textField.fieldItem.TextFieldItemState
import com.foodsaver.app.presentation.addAddress.ProfileAddAddressEvent
import com.foodsaver.app.presentation.addAddress.ProfileAddAddressState
import com.foodsaver.app.presentation.addAddress.ProfileAddAddressViewModel
import com.foodsaver.app.presentation.featureProfile.component.AddAddressLabelCard
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_address_appartment_text_field_label
import foodsaver.composeapp.generated.resources.add_address_label_as
import foodsaver.composeapp.generated.resources.add_address_label_home
import foodsaver.composeapp.generated.resources.add_address_label_other
import foodsaver.composeapp.generated.resources.add_address_label_work
import foodsaver.composeapp.generated.resources.add_address_post_code_text_field_label
import foodsaver.composeapp.generated.resources.add_address_save_location
import foodsaver.composeapp.generated.resources.add_address_street_text_field_label
import foodsaver.composeapp.generated.resources.add_address_text_field_label
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.default_map
import foodsaver.composeapp.generated.resources.location_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddNewAddressScreenRoot(
    navController: NavController,
    viewModel: ProfileAddAddressViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    AddNewAddressScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddNewAddressScreenPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                AddNewAddressScreen(
                    navController = rememberNavController(),
                    state = ProfileAddAddressState(),
                    onEvent = { /*TODO()*/ }
                )
            }
        }
    }
}

@Composable
private fun AddNewAddressScreen(
    navController: NavController,
    state: ProfileAddAddressState,
    onEvent: (ProfileAddAddressEvent) -> Unit
) {

    val labelItems = listOf(
        stringResource(Res.string.add_address_label_home),
        stringResource(Res.string.add_address_label_work),
        stringResource(Res.string.add_address_label_other),
    )

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            Box {
                Image(
                    painter = painterResource(Res.drawable.default_map),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                PrimaryFabButton(
                    onClick = {
                        navController.navigateUp()
                    },
                    background = FoodSaverTheme.colorScheme.onBackgroundSecondary,
                    modifier = Modifier
                        .padding(start = 24.dp, top = 24.dp)
                        .statusBarsPadding()
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.back_icon),
                        contentDescription = null,
                        tint = FoodSaverTheme.colorScheme.backgroundSecondary
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(30.dp))
            TextFieldItem(
                state = TextFieldItemState(
                    title = Res.string.add_address_text_field_label,
                    state = PrimaryTextFieldState(
                        value = state.fullAddress,
                        onValueChange = {
                            onEvent(ProfileAddAddressEvent.OnFullAddressChange(it))
                        },
                        placeholder = "",
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.location_icon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        maxLines = 2
                    )
                ),
                minHeight = 50.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row(Modifier.padding(horizontal = 24.dp)) {

                TextFieldItem(
                    state = TextFieldItemState(
                        title = Res.string.add_address_street_text_field_label,
                        state = PrimaryTextFieldState(
                            value = state.street,
                            onValueChange = {
                                onEvent(ProfileAddAddressEvent.OnStreetChange(it))
                            },
                            placeholder = "",
                            maxLines = 1
                        )
                    ),
                    isFullWidth = false,
                    minHeight = 50.dp,
                    modifier = Modifier
                        .weight(1f)
                )
                Spacer(Modifier.width(15.dp))
                TextFieldItem(
                    state = TextFieldItemState(
                        title = Res.string.add_address_post_code_text_field_label,
                        state = PrimaryTextFieldState(
                            value = state.postCode,
                            onValueChange = {
                                onEvent(ProfileAddAddressEvent.OnPostCodeChange(it))
                            },
                            placeholder = "",
                            maxLines = 1,
                            keyboardType = KeyboardType.Number
                        )
                    ),
                    isFullWidth = false,
                    minHeight = 50.dp,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))
            TextFieldItem(
                state = TextFieldItemState(
                    title = Res.string.add_address_appartment_text_field_label,
                    state = PrimaryTextFieldState(
                        value = state.apartment,
                        onValueChange = {
                            onEvent(ProfileAddAddressEvent.OnApartmentChange(it))
                        },
                        placeholder = "",
                        maxLines = 1
                    )
                ),
                minHeight = 50.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.add_address_label_as).uppercase(),
                color = FoodSaverTheme.colorScheme.onBackgroundSecondary,
                style = FoodSaverTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(12.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(labelItems) { index, label ->
                    AddAddressLabelCard(
                        isSelected = state.labelAsIndex == index,
                        label = label,
                        onClick = {
                            onEvent(ProfileAddAddressEvent.OnLabelChange(index, label))
                        }
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            PrimaryButton(
                onClick = {
                    onEvent(ProfileAddAddressEvent.OnSave)
                },
                text = stringResource(Res.string.add_address_save_location),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(30.dp))
        }
    }
}