package com.foodsaver.app.addProductModule.presentation.addProduct

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreProductModule.domain.model.AddProductModel
import com.foodsaver.app.coreProductModule.domain.usecase.AddProductUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val addProductUseCase: AddProductUseCase
): BaseViewModel<AddProductAction>() {

    override val baseChannel: Channel<AddProductAction> = Channel()

    private val _state = MutableStateFlow(AddProductState())
    val state = _state
        .asStateFlow()

    fun onEvent(event: AddProductEvent) {
        when (event) {
            AddProductEvent.OnAddClick -> {
                if (
                    _state.value.title.isBlank() ||
                    _state.value.description.isBlank() ||
                    _state.value.cost.isBlank() ||
                    _state.value.costUnit.isBlank() ||
                    _state.value.count.isBlank() ||
                    _state.value.unit.isBlank() ||
                    _state.value.unitName.isBlank()
//                    _state.value.expiresAt.isBlank()
                ) {
                    trySendError("Something empty!")
                    return
                }

                viewModelScope.launch(Dispatchers.InputOutput) {
                    val addProductModel = AddProductModel(
                        title = _state.value.title,
                        description = _state.value.description,
                        photo = byteArrayOf(),
                        cost = _state.value.cost.toFloat(),
                        costUnit = _state.value.costUnit,
                        categoryIds = _state.value.selectedCategories,
                        count = _state.value.count.toLong(),
                        unit = _state.value.unit.toLong(),
                        unitName = _state.value.unitName,
                        expiresAt = "_state.value.expiresAt"
                    )
                    addProductUseCase.invoke(addProductModel)
                }
            }
            is AddProductEvent.OnCostChange -> {
                _state.update { it.copy(
                    cost = event.value
                ) }
            }
            is AddProductEvent.OnCostUnitChange -> {
                _state.update { it.copy(
                    costUnit = event.value
                ) }
            }
            is AddProductEvent.OnCountChange -> {
                _state.update { it.copy(
                    count = event.value
                ) }
            }
            is AddProductEvent.OnDescriptionChange -> {
                _state.update { it.copy(
                    description = event.value
                ) }
            }
            is AddProductEvent.OnExpiresAtChange -> {
                _state.update { it.copy(
//                    expiresAt = event.value
                ) }
            }
            is AddProductEvent.OnTitleChange -> {
                _state.update { it.copy(
                    title = event.value
                ) }
            }
            is AddProductEvent.OnUnitChange -> {
                _state.update { it.copy(
                    unit = event.value
                ) }
            }
            is AddProductEvent.OnUnitNameChange -> {
                _state.update { it.copy(
                    unitName = event.value
                ) }
            }
        }
    }

    override fun mapBaseError(message: String): AddProductAction {
        return AddProductAction.OnError(message)
    }
}