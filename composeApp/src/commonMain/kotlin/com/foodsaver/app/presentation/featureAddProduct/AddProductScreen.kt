package com.foodsaver.app.presentation.featureAddProduct

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductAction
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductEvent
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductState
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductViewModel
import com.foodsaver.app.common.PrimaryButton
import com.foodsaver.app.common.PrimaryCenterAlignedTopAppBar
import com.foodsaver.app.presentation.featureAddProduct.component.AddProductField
import com.foodsaver.app.presentation.featureAddProduct.component.AddProductTextField
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.addProduct
import foodsaver.composeapp.generated.resources.addProductCategories
import foodsaver.composeapp.generated.resources.addProductCost
import foodsaver.composeapp.generated.resources.addProductCostUnit
import foodsaver.composeapp.generated.resources.addProductCount
import foodsaver.composeapp.generated.resources.addProductDescription
import foodsaver.composeapp.generated.resources.addProductExpiresAt
import foodsaver.composeapp.generated.resources.addProductIngredients
import foodsaver.composeapp.generated.resources.addProductTitle
import foodsaver.composeapp.generated.resources.addProductUnit
import foodsaver.composeapp.generated.resources.addProductUnitName
import foodsaver.composeapp.generated.resources.ic_check_icon
import foodsaver.composeapp.generated.resources.ic_plus_icon
import foodsaver.composeapp.generated.resources.save
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddProductScreenRoot(
    navController: NavController,
    addProductViewModel: AddProductViewModel = koinViewModel(),
) {

    val state by addProductViewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    ObserveActions(addProductViewModel.channel) {
        when (it) {
            is AddProductAction.OnError -> {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    AddProductScreen(
        navController = navController,
        state = state,
        onEvent = addProductViewModel::onEvent,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun AddProductScreen(
    navController: NavController,
    state: AddProductState,
    onEvent: (AddProductEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {

    val fields = listOf(
        AddProductField(
            value = state.title,
            onValueChange = { onEvent(AddProductEvent.OnTitleChange(it)) },
            placeHolder = stringResource(Res.string.addProductTitle)
        ),
        AddProductField(
            value = state.description,
            onValueChange = { onEvent(AddProductEvent.OnDescriptionChange(it)) },
            placeHolder = stringResource(Res.string.addProductDescription),
            maxLines = 3
        ),
        AddProductField(
            value = state.count,
            onValueChange = { onEvent(AddProductEvent.OnCountChange(it)) },
            keyboardType = KeyboardType.Decimal,
            placeHolder = stringResource(Res.string.addProductCount)
        ),
        AddProductField(
            value = state.unit,
            onValueChange = { onEvent(AddProductEvent.OnUnitChange(it)) },
            keyboardType = KeyboardType.Decimal,
            placeHolder = stringResource(Res.string.addProductUnit)
        ),
        AddProductField(
            value = state.unitName,
            onValueChange = { onEvent(AddProductEvent.OnUnitNameChange(it)) },
            trailingIcon = {},
            onTrailingIconClick = {},
            placeHolder = stringResource(Res.string.addProductUnitName)
        ),
        AddProductField(
            value = state.cost,
            onValueChange = { onEvent(AddProductEvent.OnCostChange(it)) },
            keyboardType = KeyboardType.Decimal,
            placeHolder = stringResource(Res.string.addProductCost)
        ),
        AddProductField(
            value = state.costUnit,
            onValueChange = { onEvent(AddProductEvent.OnCostUnitChange(it)) },
            trailingIcon = {},
            onTrailingIconClick = {},
            placeHolder = stringResource(Res.string.addProductCostUnit)
        ),
        AddProductField(
            value = "",
            onValueChange = {  },
            isFieldEnabled = false,
            trailingIcon = {
                Column {
                    Image(
                        painter = painterResource(Res.drawable.ic_plus_icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.Black),
                        modifier = Modifier
                            .size(16.dp)
                    )

                    DropdownMenu(
                        expanded = state.isCategoryDropDownMenuVisible,
                        onDismissRequest = {
                            onEvent(
                                AddProductEvent.OnDropDownMenuChange(
                                    item = AddProductEvent.DropDownMenuItems.CATEGORY,
                                    value = false
                                )
                            )
                        }
                    ) {
                        state.categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        Text(
                                            text = category.categoryName
                                        )

                                        if (state.selectedCategories.contains(category)) {

                                            Icon(
                                                painterResource(Res.drawable.ic_check_icon),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                },
                                onClick = {
//                                    onEvent(
//                                        AddProductEvent.OnDropDownMenuChange(
//                                            item = AddProductEvent.DropDownMenuItems.CATEGORY,
//                                            value = false
//                                        )
//                                    )
                                    onEvent(
                                        AddProductEvent.OnCategoryChange(category)
                                    )
                                }
                            )
                        }
                    }
                }
            },
            onTrailingIconClick = {
                onEvent(
                    AddProductEvent.OnDropDownMenuChange(
                        item = AddProductEvent.DropDownMenuItems.CATEGORY,
                        value = true
                    )
                )
            },
            placeHolder = stringResource(Res.string.addProductCategories)
        ),
        AddProductField(
            value = state.ingredients,
            onValueChange = { value -> onEvent(AddProductEvent.OnIngredientsChange(value)) },
            placeHolder = stringResource(Res.string.addProductIngredients)
        ),
        AddProductField(
            value = state.expiresAt,
            onValueChange = { onEvent(AddProductEvent.OnExpiresAtChange(it)) },
            trailingIcon = {},
            onTrailingIconClick = {},
            keyboardType = KeyboardType.Number,
            placeHolder = stringResource(Res.string.addProductExpiresAt),
            isError = state.isExpiresAtError
        ),
    )

    if (state.isGalleryPickerVisible) {
        GalleryPickerLauncher(
            onPhotosSelected = { photos ->
                val photo = photos.firstOrNull()
                photo?.let { photo ->
                    val bytes = photo.loadBytes()
                    onEvent(AddProductEvent.OnPickedImageChange(bytes))
                    onEvent(AddProductEvent.OnGalleryPickerVisibilityChange(false))
                }
            },
            onError = {
                onEvent(AddProductEvent.OnGalleryPickerVisibilityChange(false))
            },
            onDismiss = {
                onEvent(AddProductEvent.OnGalleryPickerVisibilityChange(false))
            },
            selectionLimit = 1L,
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            PrimaryCenterAlignedTopAppBar(
                title = stringResource(Res.string.addProduct),
                onNavigationButtonClick = {
                    navController.navigateUp()
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.ime),
        containerColor = FoodSaverTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize(),
    ) { paddingValues ->
        Spacer(Modifier.height(20.dp))

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {

                items(
                    items = fields
                ) { field ->
                    AddProductTextField(
                        addProductField = field,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))
                }

                item {
                    PrimaryButton(
                        text = "Add Image",
                        onClick = {
                            onEvent(AddProductEvent.OnGalleryPickerVisibilityChange(true))
                        }
                    )
                }
            }

            Spacer(Modifier.height(25.dp))

            PrimaryButton(
                text = stringResource(Res.string.save),
                onClick = {
                    onEvent(AddProductEvent.OnAddClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 55.dp)
            )
        }
    }
}