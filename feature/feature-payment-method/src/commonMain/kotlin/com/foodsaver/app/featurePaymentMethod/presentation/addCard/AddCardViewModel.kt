package com.foodsaver.app.featurePaymentMethod.presentation.addCard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodRequest
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

                if (event.value.text.length > 16) return

                _state.update {
                    it.copy(
                        cardNumber = event.value
                    )
                }
            }

            is AddCardEvent.OnCvcChange -> {
                if (event.value.text.length > 3) return
                _state.update {
                    it.copy(
                        cvc = event.value
                    )
                }
            }

            is AddCardEvent.OnExpiresDateChange -> {

                if (event.value.text.length > 6) return
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

                val expiresDate = parseCardExpiry(_state.value.expiresDate.text)
                    ?: return

                _state.update { it.copy(
                    isLoading = true
                ) }
                viewModelScope.launch {
                    editPaymentMethodRepository.addPaymentMethod(
                        methodModel = AddPaymentMethodRequest(
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

    fun parseCardExpiry(expiry: String): Instant? {
        return try {
            if (expiry.length != 6) return null

            val month = expiry.substring(0, 2).toInt()
            val year = expiry.substring(2, 6).toInt()

            if (month !in 1..12) return null

            //                        год, месяц, день
            // Формируем ISO строку: "2026-01-01T00:00:00Z"
            val isoString = "${year}-${month.toString().padStart(2, '0')}-01T00:00:00Z"
            Instant.parse(isoString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}