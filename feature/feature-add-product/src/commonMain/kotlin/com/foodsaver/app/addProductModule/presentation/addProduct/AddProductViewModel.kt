package com.foodsaver.app.addProductModule.presentation.addProduct

import androidx.compose.ui.text.TextRange
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
    private val addProductUseCase: AddProductUseCase,
    private val categoryRepository: CategoryRepository
) : BaseViewModel<AddProductAction>() {

    override val baseChannel: Channel<AddProductAction> = Channel()
    override val channel: Flow<AddProductAction> = baseChannel.receiveAsFlow()

    private val _state = MutableStateFlow(AddProductState())
    val state = _state
        .asStateFlow()

    init {
        getAllCategories()
    }

    private fun getAllCategories() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            categoryRepository.getAllCategories().onSuccess { categories ->
                _state.update {
                    it.copy(categories = categories)
                }
            }
        }
    }

    fun onEvent(event: AddProductEvent) {
        when (event) {
            AddProductEvent.OnAddClick -> {

                val currentState = _state.value

                if (
                    currentState.title.text.isBlank() ||
                    currentState.description.text.isBlank() ||
                    currentState.cost.text.isBlank() ||
                    currentState.costUnit.text.isBlank() ||
                    currentState.count.text.isBlank() ||
                    currentState.unit.text.isBlank() ||
                    currentState.unitName.text.isBlank() ||
                    currentState.expiresAt.text.isBlank() ||
                    currentState.ingredients.text.isEmpty() ||
                    currentState.pickedImageBytes == null
                ) {
                    trySendError("Something empty!")
                    return
                }

                val dateUtils = DateUtils()
                val expiresAt = currentState.expiresAt.text.replace("-", "")
                val date = dateUtils.parseToLocalDate(expiresAt) ?: run {
                    _state.update { it.copy(isExpiresAtError = true) }
                    trySendError("Wrong date format!")
                    return
                }
                val ingredients = currentState.ingredients.text
                    .split(",")
                    .map { it.trim() /*Remove spacings*/ }
                    .filter { it.isNotEmpty() /*Remove empty values*/ }

                viewModelScope.launch(Dispatchers.InputOutput) {
                    val addProductModel = AddProductModel(
                        title = currentState.title.text,
                        description = currentState.description.text,
                        photo = currentState.pickedImageBytes.bytes,
                        cost = currentState.cost.text.toFloat(),
                        costUnit = currentState.costUnit.text,
                        categoryIds = currentState.selectedCategories.map { it.categoryId },
                        count = currentState.count.text.toLong(),
                        unit = currentState.unit.text.toLong(),
                        unitName = currentState.unitName.text,
                        ingredients = ingredients,
                        expiresAt = date
                    )
                    addProductUseCase.invoke(addProductModel)
                        .onFailure {
                            println(it.uiText.asString())
                            sendError(it)
                        }.onSuccess {
                            sendError("Success!")
                        }
                }
            }

            is AddProductEvent.OnCostChange -> {
                _state.update {
                    it.copy(
                        cost = event.value
                    )
                }
            }

            is AddProductEvent.OnCostUnitChange -> {
                _state.update {
                    it.copy(
                        costUnit = event.value
                    )
                }
            }

            is AddProductEvent.OnCountChange -> {
                _state.update {
                    it.copy(
                        count = event.value
                    )
                }
            }

            is AddProductEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = event.value
                    )
                }
            }

            is AddProductEvent.OnExpiresAtChange -> {

                val textFieldValue = event.value
                val digits = textFieldValue.text.filter { it.isDigit() }
                val maxSymbols = 8

                if (digits.length > maxSymbols) return


                val newValue = buildString {
                    for (i in digits.indices) {
                        append(digits[i])

                        if ((i == 1 || i == 3) && i != digits.lastIndex) {
                            append('-')
                        }
                    }
                }

                val originCursorPosition = textFieldValue.selection.start
                val dashes = newValue.take(originCursorPosition).count { it == '-' }
                val textRange = TextRange(digits.length + dashes)

                _state.update { currentState ->

                    currentState.copy(expiresAt = textFieldValue.copy(
                        text = newValue,
                        selection = textRange
                    ))
                }
            }

            is AddProductEvent.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = event.value
                    )
                }
            }

            is AddProductEvent.OnUnitChange -> {
                _state.update {
                    it.copy(
                        unit = event.value
                    )
                }
            }

            is AddProductEvent.OnUnitNameChange -> {
                _state.update {
                    it.copy(
                        unitName = event.value
                    )
                }
            }

            is AddProductEvent.OnDropDownMenuChange -> {
                _state.update { currentState ->
                    when (event.item) {
                        AddProductEvent.DropDownMenuItems.UNIT_NAME -> currentState.copy(
                            isUnitNameDropDownMenuVisible = event.value
                        )

                        AddProductEvent.DropDownMenuItems.COST_UNIT -> currentState.copy(
                            isCostUnitDropDownMenuVisible = event.value
                        )

                        AddProductEvent.DropDownMenuItems.EXPIRES_AT -> currentState.copy(
                            isExpiresAtDropDownMenuVisible = event.value
                        )

                        AddProductEvent.DropDownMenuItems.CATEGORY -> currentState.copy(
                            isCategoryDropDownMenuVisible = event.value
                        )
                    }
                }
            }

            is AddProductEvent.OnCategoryChange -> {
                if (_state.value.selectedCategories.contains(event.category)) {
                    _state.update {
                        it.copy(selectedCategories = it.selectedCategories - event.category)
                    }
                } else {
                    _state.update {
                        it.copy(selectedCategories = it.selectedCategories + event.category)
                    }
                }
            }

            is AddProductEvent.OnIngredientsChange -> {
                println("Ingredients event ${event.value}")
                _state.update { it.copy(ingredients = event.value) }
                println("Ingredients state ${_state.value.ingredients}")
            }

            is AddProductEvent.OnGalleryPickerVisibilityChange -> {
                _state.update { it.copy(isGalleryPickerVisible = event.value) }
            }

            is AddProductEvent.OnPickedImageChange -> {
                _state.update { it.copy(pickedImageBytes = PickedImageBytes(event.value)) }
            }
        }
    }

    override fun mapBaseError(message: String): AddProductAction {
        return AddProductAction.OnError(message)
    }
}