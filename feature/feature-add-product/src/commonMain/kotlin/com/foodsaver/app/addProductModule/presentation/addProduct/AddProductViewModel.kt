package com.foodsaver.app.addProductModule.presentation.addProduct

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.addProductModule.domain.model.AddProductRequest
import com.foodsaver.app.addProductModule.domain.repository.AddProductRepository
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreCategory.domain.repository.CategoryRepository
import com.foodsaver.app.coreIngredients.domain.repository.IngredientRepository
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProductModule.domain.model.UpdateProductRequest
import com.foodsaver.app.coreProductModule.domain.repository.EditProductRepository
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.coreProductModule.domain.usecase.AddProductUseCase
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Instant

class AddProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val addProductRepository: AddProductRepository,
    private val productRepository: ReadProductRepository,
    private val editProductRepository: EditProductRepository
    ) : BaseViewModel<AddProductAction>() {

    override val baseChannel: Channel<AddProductAction> = Channel()
    override val channel: Flow<AddProductAction> = baseChannel.receiveAsFlow()

    private val navArgs = savedStateHandle.toRoute<Route.ManagerGraph.AddProductScreen>()

    private val _state = MutableStateFlow(AddProductState())
    val state = _state
        .asStateFlow()

    private var initialProduct: ProductModel? = null

    init {

        navArgs.productId?.let { productId ->
            getProduct(productId)
        }

        getAllCategories()
        getAllIngredients()
        getCurrencies()
    }

    private fun getProduct(productId: String) {
        viewModelScope.launch {
            productRepository.fetchProductById(productId).onSuccess { product ->
                _state.update {
                    it.copy(
                        product = product
                    )
                }
                initialProduct = product
            }
        }
    }

    private fun getCurrencies() {
        viewModelScope.launch {
            addProductRepository.fetchCurrencies().onSuccess { currencies ->
                _state.update {
                    it.copy(
                        currencies = currencies
                    )
                }
            }
        }
    }

    private fun getAllIngredients() {
        viewModelScope.launch {
            ingredientRepository.fetchAllIngredients().onSuccess { ingredients ->
                _state.update {
                    it.copy(
                        allIngredients = ingredients
                    )
                }
            }
        }
    }

    private fun getAllCategories() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            categoryRepository.fetchAllCategories().onSuccess { categories ->
                _state.update {
                    it.copy(
                        allCategories = categories
                    )
                }
            }
        }
    }

    fun onEvent(event: AddProductEvent) {
        when (event) {
            is AddProductEvent.OnChangeGalleryPickerVisibility -> {
                _state.update {
                    it.copy(
                        isGalleryPickerVisible = event.isVisible
                    )
                }
            }

            is AddProductEvent.OnCountChange -> {
                val count = event.value.toLongOrNull() ?: return
                _state.update {
                    it.copy(
                        product = it.product?.copy(count = count),
                        count = count
                    )
                }
            }

            is AddProductEvent.OnCurrencyChange -> {
                _state.update {
                    it.copy(
                        product = it.product?.copy(currency = event.value),
                        currency = event.value
                    )
                }
            }

            is AddProductEvent.OnDetailsChange -> {
                _state.update {
                    it.copy(
                        product = it.product?.copy(description = event.value),
                        details = event.value
                    )
                }
            }

            is AddProductEvent.OnDiscountChange -> {
                val discount = event.value.toDoubleOrNull() ?: return
                _state.update {
                    it.copy(
                        product = it.product?.copy(discount = discount),
                        discount = discount
                    )
                }
            }

            is AddProductEvent.OnExpiresDateChange -> {
                val digitsOnly = event.value.filter { it.isDigit() }
                val expiresDate = parseExpiresDateToInstant(digitsOnly)
                if (digitsOnly.length <= 8) {
                    _state.update {
                        it.copy(
                            product = if (expiresDate != null) it.product?.copy(expiresAt = expiresDate)
                            else it.product,
                            expiresDate = digitsOnly
                        )
                    }
                }
            }

            is AddProductEvent.OnIsDeliveryPriceChange -> {
                _state.update {
                    it.copy(
                        isDeliveryPrice = event.value
                    )
                }
            }

            is AddProductEvent.OnIsPickUpPriceChange -> {
                _state.update {
                    it.copy(
                        isPickUpPrice = event.value
                    )
                }
            }

            is AddProductEvent.OnNameChange -> {
                _state.update {
                    it.copy(
                        product = it.product?.copy(name = event.value),
                        name = event.value
                    )
                }
            }

            is AddProductEvent.OnPickCategory -> {
                _state.update { currentState ->
                    val updatedCategories =
                        if (currentState.selectedCategoryIds.contains(event.categoryId)) {
                            currentState.selectedCategoryIds - event.categoryId
                        } else {
                            currentState.selectedCategoryIds + event.categoryId
                        }
                    currentState.copy(
                        product = currentState.product?.copy(categoryIds = updatedCategories),
                        selectedCategoryIds = updatedCategories
                    )
                }
            }

            is AddProductEvent.OnPickImages -> {
                event.images.forEach { image ->
                    viewModelScope.launch {
                        addProductRepository.uploadImage(image, _state.value.product?.productId).onSuccess { response ->
                            _state.update {
                                it.copy(
                                    product = it.product?.copy(imageUris = it.product.imageUris + response.absoluteUri),
                                    productImageUris = it.productImageUris + response
                                )
                            }
                        }
                    }
                }
            }

            is AddProductEvent.OnPickIngredient -> {
                _state.update { currentState ->
                    val updatedIngredients =
                        if (currentState.selectedIngredientIds.contains(event.ingredientId)) {
                            currentState.selectedIngredientIds - event.ingredientId
                        } else {
                            currentState.selectedIngredientIds + event.ingredientId
                        }
                    currentState.copy(
                        product = currentState.product?.copy(ingredientIds = updatedIngredients),
                        selectedIngredientIds = updatedIngredients
                    )
                }
            }

            is AddProductEvent.OnPriceChange -> {
                val price = event.value.toDoubleOrNull() ?: return
                _state.update {
                    it.copy(
                        product = it.product?.copy(price = price),
                        price = price
                    )
                }
            }

            AddProductEvent.OnReset -> {
                _state.update {
                    it.copy(
                        name = "",
                        details = "",
                        productImageUris = emptyList(),
                        isGalleryPickerVisible = false,
                        expiresDate = "",
                        price = null,
                        count = 1L,
                        unit = null,
                        isPickUpPrice = true,
                        isDeliveryPrice = false,
                        discount = null,
                        currency = null,
                        selectedIngredientIds = emptyList(),
                        selectedCategoryIds = emptyList(),

                        product = initialProduct
                    )
                }
            }

            AddProductEvent.OnSave -> {
                val currentState = _state.value
                val product = currentState.product

                if (product != null) {
                    // update existing product
                    val request = with(product) {
                        UpdateProductRequest(
                            productId = productId,
                            name = name,
                            description = description,
                            imageUris = imageUris,
                            price = price,
                            discount = discount,
                            count = count,
                            unit = unit,
                            currency = currency,
                            isAvailable = isAvailable,
                            isDeleted = isDeleted,
                            ingredientIds = ingredientIds,
                            categoryIds = categoryIds
                        )
                    }

                    viewModelScope.launch {
                        editProductRepository.updateProduct(request).onSuccess {
                            baseChannel.send(AddProductAction.OnSuccessUpsert)
                        }
                    }
                } else {
                    // create new product
                    with(currentState) {
                        if (name.isBlank()) {
                            sendError(ApiResult.error(AddProductLocalError.EmptyName))
                            return
                        }

                        if (productImageUris.isEmpty()) {
                            sendError(ApiResult.error(AddProductLocalError.EmptyImages))
                            return
                        }

                        if (price == null) {
                            sendError(ApiResult.error(AddProductLocalError.EmptyPrice))
                            return
                        }
                        if (price <= 0.0) {
                            sendError(ApiResult.error(AddProductLocalError.LowPrice))
                            return
                        }

                        if (currency.isNullOrBlank()) {
                            sendError(ApiResult.error(AddProductLocalError.EmptyCurrency))
                            return
                        }

                        if (expiresDate.isBlank()) {
                            sendError(ApiResult.error(AddProductLocalError.EmptyExpiresDate))
                            return
                        }

                        if (!isValidDate(expiresDate)) {
                            sendError(ApiResult.error(AddProductLocalError.ExpiresDateMismatch))
                            return
                        }

                        if (unit == null) {
                            sendError(ApiResult.error(AddProductLocalError.EmptyUnit))
                            return
                        }

                        if (selectedCategoryIds.isEmpty()) {
                            sendError(ApiResult.error(AddProductLocalError.EmptyCategory))
                            return
                        }
                        if (selectedIngredientIds.isEmpty()) {
                            sendError(ApiResult.error(AddProductLocalError.EmptyIngredients))
                            return
                        }

                        val expiresDateInstant = parseExpiresDateToInstant(expiresDate) ?: run {
                            sendError(ApiResult.error(AddProductLocalError.ExpiresDateMismatch))
                            return
                        }

                        val request = AddProductRequest(
                            name = name,
                            description = details,
                            imageUris = productImageUris.map { it.relativeUri },
                            expiresAt = expiresDateInstant,
                            price = price,
                            count = count,
                            unit = unit.name,
                            discount = discount ?: 0.0,
                            currency = currency,
                            isAvailable = true,
                            ingredientIds = selectedIngredientIds,
                            categoryIds = selectedCategoryIds
                        )

                        viewModelScope.launch {
                            addProductRepository.addProduct(request).onSuccess {
                                baseChannel.send(AddProductAction.OnSuccessUpsert)
                            }
                        }
                    }
                }


            }

            is AddProductEvent.OnUnitChange -> {
                _state.update {
                    it.copy(
                        product = it.product?.copy(unit = event.value.name),
                        unit = event.value
                    )
                }
            }

            AddProductEvent.OnDelete -> {
                val productId = navArgs.productId ?: run {
                    baseChannel.trySend(AddProductAction.OnSuccessUpsert)
                    return
                }

                viewModelScope.launch {
                    editProductRepository.deleteProduct(productId).onSuccess {
                        baseChannel.send(AddProductAction.OnSuccessUpsert)
                    }
                }
            }
        }
    }

    override fun mapBaseError(message: String): AddProductAction {
        return AddProductAction.OnError(message)
    }

    private fun isValidDate(dateStr: String): Boolean {
        if (dateStr.length != 8) return false
        val day = dateStr.substring(0, 2).toIntOrNull() ?: return false
        val month = dateStr.substring(2, 4).toIntOrNull() ?: return false
        val year = dateStr.substring(4, 8).toIntOrNull() ?: return false

        if (month !in 1..12) return false
        if (day !in 1..31) return false

        // Быстрая проверка дней в феврале/апреле и т.д.
        val daysInMonth = when (month) {
            2 -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }

        return day <= daysInMonth
    }

    private fun parseExpiresDateToInstant(expiresDate: String): Instant? {
        // 1. Извлекаем компоненты из строки "ДДММГГГГ" (например, "19052026")
        try {
            val day = expiresDate.substring(0, 2)
            val month = expiresDate.substring(2, 4)
            val year = expiresDate.substring(4, 8)

            // 2. Собираем валидную ISO-8601 строку с UTC смещением (Z)
            // Результат: "2026-05-19T00:00:00Z"
            val isoDateTimeString = "${year}-${month}-${day}T00:00:00Z"

            // 3. Используем новый метод из stdlib Kotlin 2.3
            return Instant.parse(isoDateTimeString)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}