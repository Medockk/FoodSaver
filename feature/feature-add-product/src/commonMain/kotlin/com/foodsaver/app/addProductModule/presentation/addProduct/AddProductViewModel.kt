package com.foodsaver.app.addProductModule.presentation.addProduct

import androidx.compose.ui.text.TextRange
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.commonModule.utils.DateUtils
import com.foodsaver.app.coreCategory.domain.repository.CategoryRepository
import com.foodsaver.app.coreProductModule.domain.model.AddProductModel
import com.foodsaver.app.coreProductModule.domain.usecase.AddProductUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val addProductUseCase: AddProductUseCase,
    private val categoryRepository: CategoryRepository
) : BaseViewModel<AddProductAction>() {

    override val baseChannel: Channel<AddProductAction> = Channel()
    override val channel: Flow<AddProductAction> = baseChannel.receiveAsFlow()

    private val navArgs = savedStateHandle.toR

    private val _state = MutableStateFlow(AddProductState())
    val state = _state
        .asStateFlow()

    init {
        getAllCategories()
    }

    private fun getAllCategories() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            categoryRepository.getAllCategories().onSuccess { categories ->
                _state.update { it.copy(
                    allCategories = categories
                ) }
            }
        }
    }

    fun onEvent(event: AddProductEvent) {
        when (event) {
            is AddProductEvent.OnChangeGalleryPickerVisibility -> {
                _state.update { it.copy(
                    isGalleryPickerVisible = event.isVisible
                ) }
            }
            is AddProductEvent.OnCountChange -> {
                TODO()
            }
            is AddProductEvent.OnCurrencyChange -> {
                _state.update { it.copy(
                    currency = event.value
                ) }
            }
            is AddProductEvent.OnDetailsChange -> {
                _state.update { it.copy(
                    details = event.value
                ) }
            }
            is AddProductEvent.OnDiscountChange -> {
                TODO()
            }
            is AddProductEvent.OnExpiresDateChange -> {
                _state.update { it.copy(
                    expiresDate = event.value
                ) }
            }
            is AddProductEvent.OnIsDeliveryPriceChange -> {
                _state.update { it.copy(
                    isDeliveryPrice = event.value
                ) }
            }
            is AddProductEvent.OnIsPickUpPriceChange -> {
                _state.update { it.copy(
                    isPickUpPrice = event.value
                ) }
            }
            is AddProductEvent.OnNameChange -> {
                _state.update { it.copy(
                    name = event.value
                ) }
            }
            is AddProductEvent.OnPickCategory -> {
                TODO()
            }
            is AddProductEvent.OnPickImages -> {
                TODO("Send request to add image in server and return uri")
            }
            is AddProductEvent.OnPickIngredient -> {
                TODO()
            }
            is AddProductEvent.OnPriceChange -> {
               TODO()
            }
            AddProductEvent.OnReset -> TODO()
            AddProductEvent.OnSave -> TODO()
            is AddProductEvent.OnUnitChange -> {
                _state.update { it.copy(
                    unit = event.value
                ) }
            }
        }
    }

    override fun mapBaseError(message: String): AddProductAction {
        return AddProductAction.OnError(message)
    }
}