package com.foodsaver.app.presentation.featureManager

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductEvent
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductState
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductViewModel
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.common.button.PrimaryTextButton
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.add_new_items
import foodsaver.composeapp.generated.resources.details
import foodsaver.composeapp.generated.resources.reset
import foodsaver.composeapp.generated.resources.save_changes
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddProductScreenRoot(
    navController: NavController,
    viewModel: AddProductViewModel = koinViewModel()
) {

}

@Composable
private fun AddProductScreen(
    navController: NavController,
    state: AddProductState,
    onEvent: (AddProductEvent) -> Unit
) {

    if (state.isGalleryPickerVisible) {
        GalleryPickerLauncher(
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
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.add_new_items),
                onNavigationClick = {
                    navController.navigateUp()
                },
                actions = {
                    PrimaryTextButton(
                        onClick = {
                            onEvent(AddProductEvent.OnReset)
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.reset).uppercase(),
                            color = FoodSaverTheme.colorScheme.primary,
                            style = FoodSaverTheme.typography.bodySmall
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = paddingValues
        ) {

            // details
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(Res.string.details).uppercase(),
                    color = FoodSaverTheme.colorScheme.onBackground,
                    style = FoodSaverTheme.typography.bodySmall
                )
                Spacer(Modifier.height(7.dp))

            }

            // save button
            item {
                Spacer(Modifier.height(30.dp))
                PrimaryButton(
                    onClick = {
                        onEvent(AddProductEvent.OnSave)
                    },
                    text = stringResource(Res.string.save_changes).uppercase()
                )
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}