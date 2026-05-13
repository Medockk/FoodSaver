package com.foodsaver.app.featurePaymentMethod.presentation.addCard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodModel
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Instant

class AddCardViewModel(
    savedStateHandle: SavedStateHandle,
    private val editPaymentMethodRepository: EditPaymentMethodRepository,
) : BaseViewModel<AddCardAction>() {

    private val navArgs = savedStateHandle.toRoute<Route.PaymentMethodGraph.AddCardScreen>()
    private val _state = MutableStateFlow(AddCardState())
    val state = _state.asStateFlow()

    fun onEvent(event: AddCardEvent) {
        when (event) {
            is AddCardEvent.OnCardHolderNameChange -> {
                _state.update {
                    it.copy(
                        cardHolderName = event.value
                    )
                }
            }

            is AddCardEvent.OnCardNumberChange -> {
                _state.update {
                    it.copy(
                        cardNumber = event.value
                    )
                }
            }

            is AddCardEvent.OnCvcChange -> {
                _state.update {
                    it.copy(
                        cvc = event.value
                    )
                }
            }

            is AddCardEvent.OnExpiresDateChange -> {
                _state.update {
                    it.copy(
                        expiresDate = event.value
                    )
                }
            }

            AddCardEvent.OnAddCard -> {
                if (
                    _state.value.cardHolderName.text.isBlank() ||
                    _state.value.cardNumber.text.isBlank() ||
                    _state.value.expiresDate.text.isBlank() ||
                    _state.value.cvc.text.isBlank()
                ) {
                    return
                }

                val typeId = navArgs.typeId
                if (typeId.isBlank()) return

                val expiresDate = try {
                    Instant.parse(_state.value.expiresDate.text)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                } ?: return

                _state.update { it.copy(
                    isLoading = true
                ) }
                viewModelScope.launch {
                    editPaymentMethodRepository.addPaymentMethod(
                        methodModel = AddPaymentMethodModel(
                            typeId = typeId,
                            cartHolderName = _state.value.cardHolderName.text,
                            cardNumber = _state.value.cardNumber.text,
                            expiresDate = expiresDate,
                            cvc = _state.value.cvc.text
                        )
                    )
                }
            }
        }
    }

    override fun mapBaseError(message: String): AddCardAction {
        return AddCardAction.OnError(message)
    }
}