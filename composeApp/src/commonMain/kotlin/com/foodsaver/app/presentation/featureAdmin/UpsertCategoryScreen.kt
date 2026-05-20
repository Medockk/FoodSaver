package com.foodsaver.app.presentation.featureAdmin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.common.button.PrimaryTextButton
import com.foodsaver.app.common.textField.PrimaryTextField
import com.foodsaver.app.common.textField.PrimaryTextFieldState
import com.foodsaver.app.common.textField.fieldItem.createLabel.CreateLabel
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.featureCategory.presentation.upsertCategoryViewModel.UpsertCategoryAction
import com.foodsaver.app.featureCategory.presentation.upsertCategoryViewModel.UpsertCategoryEvent
import com.foodsaver.app.featureCategory.presentation.upsertCategoryViewModel.UpsertCategoryState
import com.foodsaver.app.featureCategory.presentation.upsertCategoryViewModel.UpsertCategoryViewModel
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.category_delete
import foodsaver.composeapp.generated.resources.category_name
import foodsaver.composeapp.generated.resources.create_category
import foodsaver.composeapp.generated.resources.edit_category
import foodsaver.composeapp.generated.resources.save
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UpsertCategoryScreenRoot(
    onBackClick: () -> Unit,
    viewModel: UpsertCategoryViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    UpsertCategoryScreen(
        onBackClick = onBackClick,
        state = state,
        onEvent = viewModel::onEvent
    )

    ObserveActions(viewModel.channel) {
        when (it) {
            UpsertCategoryAction.OnCategoryUpserted -> {
                onBackClick()
            }
            is UpsertCategoryAction.OnError -> {}
        }
    }
}

@Composable
private fun UpsertCategoryScreen(
    onBackClick: () -> Unit,
    state: UpsertCategoryState,
    onEvent: (UpsertCategoryEvent) -> Unit
) {

    Scaffold(
        containerColor = FoodSaverTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            PrimaryTopBar(
                title = stringResource(if (state.category == null) Res.string.create_category else Res.string.edit_category),
                onNavigationClick = onBackClick,
                actions = {
                    if (state.category != null) {
                        PrimaryTextButton(
                            onClick = { onEvent(UpsertCategoryEvent.OnIsDeletedChange(true)) }
                        ) {
                            Text(
                                text = stringResource(Res.string.category_delete),
                                style = FoodSaverTheme.typography.bodySmall,
                                color = FoodSaverTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            CreateLabel(
                label = stringResource(Res.string.category_name)
            )
            PrimaryTextField(
                state = PrimaryTextFieldState(
                    value = state.category?.categoryName ?: state.name,
                    onValueChange = { onEvent(UpsertCategoryEvent.OnNameChange(it)) },
                    placeholder = ""
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )

            Spacer(Modifier.weight(1f))
            PrimaryButton(
                onClick = { onEvent(UpsertCategoryEvent.OnSave) },
                text = stringResource(Res.string.save),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(10.dp))
        }
    }
}