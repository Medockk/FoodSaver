package com.foodsaver.app.presentation.featureUpsertRestaurant

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.common.image.UploadImageRow
import com.foodsaver.app.common.textField.PrimaryTextField
import com.foodsaver.app.common.textField.PrimaryTextFieldState
import com.foodsaver.app.common.textField.fieldItem.createLabel.CreateLabel
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.featureRestaurant.featureEnterprises.presentation.upsertRestaurant.UpsertRestaurantEvent
import com.foodsaver.app.featureRestaurant.featureEnterprises.presentation.upsertRestaurant.UpsertRestaurantState
import com.foodsaver.app.featureRestaurant.featureEnterprises.presentation.upsertRestaurant.UpsertRestaurantViewModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.create_restaurant
import foodsaver.composeapp.generated.resources.edit_restaurant
import foodsaver.composeapp.generated.resources.restaurant_address_name
import foodsaver.composeapp.generated.resources.restaurant_average_delivery_time
import foodsaver.composeapp.generated.resources.restaurant_delivery_cost
import foodsaver.composeapp.generated.resources.restaurant_description
import foodsaver.composeapp.generated.resources.restaurant_latitude
import foodsaver.composeapp.generated.resources.restaurant_longitude
import foodsaver.composeapp.generated.resources.restaurant_name
import foodsaver.composeapp.generated.resources.restaurant_photos
import foodsaver.composeapp.generated.resources.save_changes
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UpsertRestaurantScreenRoot(
    onBackClick: () -> Unit,
    viewModel: UpsertRestaurantViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    UpsertRestaurantScreen(
        onBackClick = onBackClick,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun UpsertRestaurantScreen(
    onBackClick: () -> Unit,
    state: UpsertRestaurantState,
    onEvent: (UpsertRestaurantEvent) -> Unit
) {

    if (state.isGalleryPickerVisible) {
        GalleryPickerLauncher(
            allowMultiple = true,
            includeExif = true,
            onPhotosSelected = { photos ->
                val pickPhotos = photos.map { UpsertRestaurantEvent.OnPickPhoto.PickPhoto(it.loadBytes(), it.exif?.orientation) }
                onEvent(UpsertRestaurantEvent.OnPickPhoto(pickPhotos))
                onEvent(UpsertRestaurantEvent.OnChangeGalleryPickerVisibility(false))
            },
            onError = {
                onEvent(UpsertRestaurantEvent.OnChangeGalleryPickerVisibility(false))
            },
            onDismiss = {
                onEvent(UpsertRestaurantEvent.OnChangeGalleryPickerVisibility(false))
            }
        )
    }

    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            PrimaryTopBar(
                title = if (state.restaurantModel != null) stringResource(Res.string.edit_restaurant)
                else stringResource(Res.string.create_restaurant),
                onNavigationClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                CreateLabel(
                    label = stringResource(Res.string.restaurant_name),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                PrimaryTextField(
                    state = PrimaryTextFieldState(
                        value = state.restaurantModel?.name ?: state.name,
                        onValueChange = { onEvent(UpsertRestaurantEvent.OnNameChange(it)) },
                        placeholder = "",
                        maxLines = 1
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }

            item {
                CreateLabel(
                    label = stringResource(Res.string.restaurant_photos),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                UploadImageRow(
                    uris = state.restaurantModel?.photoUris ?: emptyList(),
                    onUploadClick = {
                        onEvent(UpsertRestaurantEvent.OnChangeGalleryPickerVisibility(true))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column {
                        CreateLabel(
                            label = stringResource(Res.string.restaurant_address_name),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        PrimaryTextField(
                            state = PrimaryTextFieldState(
                                value = state.restaurantModel?.addressName ?: state.addressName,
                                onValueChange = { onEvent(UpsertRestaurantEvent.OnAddressNameChange(it)) },
                                placeholder = "",
                                maxLines = 1
                            ),
                            minHeight = 40.dp
                        )
                    }
                    Column {
                        CreateLabel(
                            label = stringResource(Res.string.restaurant_latitude),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        PrimaryTextField(
                            state = PrimaryTextFieldState(
                                value = state.restaurantModel?.latitude?.toString() ?: state.latitude?.toString() ?: "",
                                onValueChange = { onEvent(UpsertRestaurantEvent.OnLatitudeChange(it)) },
                                placeholder = "",
                                maxLines = 1,
                                keyboardType = KeyboardType.Number
                            ),
                            minHeight = 40.dp
                        )
                    }
                    Column {
                        CreateLabel(
                            label = stringResource(Res.string.restaurant_longitude),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        PrimaryTextField(
                            state = PrimaryTextFieldState(
                                value = state.restaurantModel?.longitude?.toString() ?: state.longitude?.toString() ?: "",
                                onValueChange = { onEvent(UpsertRestaurantEvent.OnLongitudeChange(it)) },
                                placeholder = "",
                                maxLines = 1,
                                keyboardType = KeyboardType.Number
                            ),
                            minHeight = 40.dp
                        )
                    }

                }
            }

            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column {
                        CreateLabel(
                            label = stringResource(Res.string.restaurant_average_delivery_time),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        PrimaryTextField(
                            state = PrimaryTextFieldState(
                                value = state.restaurantModel?.averageDeliveryTime?.toString() ?: state.averageDeliveryTime?.toString() ?: "",
                                onValueChange = { onEvent(UpsertRestaurantEvent.OnAverageDeliveryTimeChange(it)) },
                                placeholder = "",
                                maxLines = 1
                            ),
                            minHeight = 40.dp
                        )
                    }
                    Column {
                        CreateLabel(
                            label = stringResource(Res.string.restaurant_delivery_cost),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        PrimaryTextField(
                            state = PrimaryTextFieldState(
                                value = state.restaurantModel?.deliveryCost?.toString() ?: state.deliveryCost?.toString() ?: "",
                                onValueChange = { onEvent(UpsertRestaurantEvent.OnDeliveryCostChange(it)) },
                                placeholder = "",
                                maxLines = 1
                            ),
                            minHeight = 40.dp
                        )
                    }
                }
            }

            item {
                CreateLabel(
                    label = stringResource(Res.string.restaurant_description),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                PrimaryTextField(
                    state = PrimaryTextFieldState(
                        value = state.restaurantModel?.description ?: state.description,
                        onValueChange = { onEvent(UpsertRestaurantEvent.OnDescriptionChange(it)) },
                        placeholder = ""
                    ),
                    minHeight = 100.dp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }

            item {
                Spacer(Modifier.height(20.dp))
                PrimaryButton(
                    onClick = {
                        onEvent(UpsertRestaurantEvent.OnSave)
                    },
                    text = stringResource(Res.string.save_changes),
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}