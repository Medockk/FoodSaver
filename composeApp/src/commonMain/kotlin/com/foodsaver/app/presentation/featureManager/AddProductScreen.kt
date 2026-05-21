package com.foodsaver.app.presentation.featureManager

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductAction
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductEvent
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductState
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductViewModel
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.common.button.PrimaryIconButton
import com.foodsaver.app.common.button.PrimaryTextButton
import com.foodsaver.app.common.textField.BorderTextField
import com.foodsaver.app.common.textField.PrimaryTextFieldState
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.presentation.featureManager.components.AddProductPriceCheckboxes
import com.foodsaver.app.presentation.featureManager.components.CategoryPicker
import com.foodsaver.app.presentation.featureManager.components.CountDiscountFields
import com.foodsaver.app.presentation.featureManager.components.ExpiresDateUnitFields
import com.foodsaver.app.presentation.featureManager.components.IngredientsPicker
import com.foodsaver.app.presentation.featureManager.components.PriceField
import com.foodsaver.app.common.image.UploadImageRow
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_new_items
import foodsaver.composeapp.generated.resources.categories
import foodsaver.composeapp.generated.resources.delete_icon
import foodsaver.composeapp.generated.resources.details
import foodsaver.composeapp.generated.resources.expires_date
import foodsaver.composeapp.generated.resources.ingredients
import foodsaver.composeapp.generated.resources.item_name
import foodsaver.composeapp.generated.resources.price
import foodsaver.composeapp.generated.resources.reset
import foodsaver.composeapp.generated.resources.reset_changes_icon
import foodsaver.composeapp.generated.resources.save_changes
import foodsaver.composeapp.generated.resources.upload_photo_or_video
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddProductScreenRoot(
    navController: NavController,
    canDeleteProduct: Boolean = true,
    viewModel: AddProductViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHost = remember { SnackbarHostState() }

    AddProductScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHost = snackbarHost
    )

    ObserveActions(viewModel.channel) {
        when (it) {
            is AddProductAction.OnError -> {
                snackbarHost.showSnackbar(it.message, withDismissAction = true)
            }

            AddProductAction.OnSuccessUpsert -> { navController.navigateUp() }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddProductScreenPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(Modifier.padding(padding)) {
                AddProductScreen(
                    navController = rememberNavController(),
                    state = AddProductState(
                        allCategories = listOf(
                            CategoryModel(
                                categoryId = "1",
                                categoryName = "Burger"
                            ),
                            CategoryModel(
                                categoryId = "TODO()",
                                categoryName = "Pizza"
                            ),
                            CategoryModel(
                                categoryId = "3",
                                categoryName = "Sushi"
                            ),
                            CategoryModel(
                                categoryId = "TODO()",
                                categoryName = "Snacks"
                            ),
                        ),
                        selectedCategoryIds = listOf("1", "3")
                    ),
                    onEvent = { /*TODO()*/ },
                    snackbarHost = SnackbarHostState()
                )
            }
        }
    }
}

@Composable
private fun AddProductScreen(
    navController: NavController,
    state: AddProductState,
    onEvent: (AddProductEvent) -> Unit,
    snackbarHost: SnackbarHostState
) {

    if (state.isGalleryPickerVisible) {
        GalleryPickerLauncher(
            allowMultiple = true,
            includeExif = true,
            onPhotosSelected = { photos ->
                val imageBytes = photos.map { it.loadBytes() }
                onEvent(AddProductEvent.OnPickImages(imageBytes))
                onEvent(AddProductEvent.OnChangeGalleryPickerVisibility(false))
            },
            onError = {
                onEvent(AddProductEvent.OnChangeGalleryPickerVisibility(false))
            },
            onDismiss = {
                onEvent(AddProductEvent.OnChangeGalleryPickerVisibility(false))
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(snackbarHost, modifier = Modifier.imePadding())
        },
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.add_new_items),
                onNavigationClick = {
                    navController.navigateUp()
                },
                actions = {
                    PrimaryIconButton(
                        onClick = { onEvent(AddProductEvent.OnReset) },
                        icon = Res.drawable.reset_changes_icon,
                        modifier = Modifier.size(24.dp)
                    )
                    if (state.product != null) {
                        PrimaryIconButton(
                            onClick = { onEvent(AddProductEvent.OnDelete) },
                            icon = Res.drawable.delete_icon,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentPadding = paddingValues
        ) {

            // name
            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = stringResource(Res.string.item_name).uppercase(),
                    style = FoodSaverTheme.typography.bodySmall,
                    color = FoodSaverTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(8.dp))
                BorderTextField(
                    value = state.product?.name ?: state.name,
                    onValueChange = { onEvent(AddProductEvent.OnNameChange(it)) },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    maxLines = 1,
                    placeholder = "...",
                    innerPadding = 0.dp
                )
            }

            // upload images
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(Res.string.upload_photo_or_video).uppercase(),
                    color = FoodSaverTheme.colorScheme.onBackground,
                    style = FoodSaverTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(16.dp))
                UploadImageRow(
                    uris = state.product?.imageUris ?: state.productImageUris.map { it.absoluteUri },
                    onUploadClick = {
                        onEvent(AddProductEvent.OnChangeGalleryPickerVisibility(true))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            // price
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(Res.string.price).uppercase(),
                    color = FoodSaverTheme.colorScheme.onBackground,
                    style = FoodSaverTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    PriceField(
                        value = state.product?.price?.toString() ?: state.price?.toString() ?: "",
                        onValueChange = { onEvent(AddProductEvent.OnPriceChange(it)) },
                        selectedCurrency = state.product?.currency ?: state.currency,
                        currencies = state.currencies,
                        onCurrencyClick = { onEvent(AddProductEvent.OnCurrencyChange(it)) },
                        modifier = Modifier
                            .padding(start = 24.dp)
                    )

                    AddProductPriceCheckboxes(
                        isPickUpChecked = state.isPickUpPrice,
                        onPickUpCheckedChange = { onEvent(AddProductEvent.OnIsPickUpPriceChange(it)) },
                        isDeliveryChecked = state.isDeliveryPrice,
                        onDeliveryCheckedChange = { onEvent(AddProductEvent.OnIsDeliveryPriceChange(it)) }
                    )
                }
            }

            // count + discount
            item {
                Spacer(Modifier.height(20.dp))
                CountDiscountFields(
                    countState = PrimaryTextFieldState(
                        value = state.product?.count?.toString() ?: state.count.toString(),
                        onValueChange = { onEvent(AddProductEvent.OnCountChange(it)) },
                        placeholder = "",
                        maxLines = 1,
                        keyboardType = KeyboardType.Number
                    ),
                    discountState = PrimaryTextFieldState(
                        value = state.product?.discount?.toString() ?: state.discount?.toString() ?: "",
                        onValueChange = { onEvent(AddProductEvent.OnDiscountChange(it)) },
                        placeholder = "%",
                        maxLines = 1,
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
            
            // expires date + unit
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(Res.string.expires_date).uppercase(),
                    color = FoodSaverTheme.colorScheme.onBackground,
                    style = FoodSaverTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(16.dp))
                ExpiresDateUnitFields(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    expiresDate = state.product?.expiresAt?.toString() ?: state.expiresDate,
                    onExpiresDateChange = {
                        onEvent(AddProductEvent.OnExpiresDateChange(it))
                    },
                    selectedUnit = state.unit,
                    onPickUnit = { unit ->
                        onEvent(AddProductEvent.OnUnitChange(unit))
                    }
                )
            }

            // categories
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(Res.string.categories).uppercase(),
                    color = FoodSaverTheme.colorScheme.onBackground,
                    style = FoodSaverTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(16.dp))
                CategoryPicker(
                    allCategories = state.allCategories,
                    selectedCategoryIds = state.product?.categoryIds ?: state.selectedCategoryIds,
                    onPickCategory = { onEvent(AddProductEvent.OnPickCategory(it.categoryId)) }
                )
            }

            // ingredients
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(Res.string.ingredients).uppercase(),
                    color = FoodSaverTheme.colorScheme.onBackground,
                    style = FoodSaverTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(16.dp))
                IngredientsPicker(
                    ingredients = state.allIngredients,
                    onIngredientClick = {
                        onEvent(AddProductEvent.OnPickIngredient(it.id))
                    }
                )
            }

            // details
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(Res.string.details).uppercase(),
                    color = FoodSaverTheme.colorScheme.onBackground,
                    style = FoodSaverTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(7.dp))
                BorderTextField(
                    value = state.product?.description ?: state.details,
                    onValueChange = {
                        onEvent(AddProductEvent.OnDetailsChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    maxLines = 3
                )
            }

            // save button
            item {
                Spacer(Modifier.height(30.dp))
                PrimaryButton(
                    onClick = {
                        onEvent(AddProductEvent.OnSave)
                    },
                    text = stringResource(Res.string.save_changes).uppercase(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}