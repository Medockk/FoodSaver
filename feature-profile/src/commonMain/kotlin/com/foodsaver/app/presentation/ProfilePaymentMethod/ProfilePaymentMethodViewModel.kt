package com.foodsaver.app.presentation.ProfilePaymentMethod

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.ApiResult.onFailure
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodModel
import com.foodsaver.app.corePaymentMethod.domain.repository.ReadPaymentMethodRepository
import com.foodsaver.app.corePaymentMethod.domain.usecase.AddPaymentMethodUseCase
import com.foodsaver.app.corePaymentMethod.domain.usecase.RemovePaymentMethodUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfilePaymentMethodViewModel(
    private val readPaymentMethodRepository: ReadPaymentMethodRepository,
    private val addPaymentMethodUseCase: AddPaymentMethodUseCase,
    private val removePaymentMethodUseCase: RemovePaymentMethodUseCase,
) : BaseViewModel<ProfilePaymentMethodAction>() {

    private val _state = MutableStateFlow(ProfilePaymentMethodState())
    val state = _state.asStateFlow()

    override val baseChannel: Channel<ProfilePaymentMethodAction> = Channel()
    val channel = baseChannel.receiveAsFlow()

    init {
        getPaymentMethods()
    }

    private fun getPaymentMethods() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            readPaymentMethodRepository.getPaymentMethod().collectRequest(
                onSuccess = { response ->
                    withContext(Dispatchers.Main) {
                        _state.update {
                            it.copy(
                                cards = response,
                                isLoading = false
                            )
                        }
                    }
                },
                onLoading = {
                    _state.update { it.copy(isLoading = true) }
                },
                onError = { error ->
                    _state.update { it.copy(isLoading = true) }
                    sendError(error.message)
                }
            )
        }
    }

    fun onEvent(event: ProfilePaymentMethodEvent) {
        when (event) {
            ProfilePaymentMethodEvent.OnAddNewCardClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    addPaymentMethodUseCase.invoke(
                        addPaymentMethodModel = AddPaymentMethodModel(
                            bank = _state.value.dialogBankName,
                            cardNumber = _state.value.dialogCardNumber,
                            isSelected = _state.value.dialogIsSelectedCard
                        )
                    ).onFailure {
                        sendError(it.message)
                    }

                    _state.update {
                        it.copy(
                            isDialogOpen = false,
                            dialogCardNumber = "",
                            dialogBankName = "",
                            dialogIsSelectedCard = false
                        )
                    }
                }
            }

            is ProfilePaymentMethodEvent.OnNewCardBankChange -> {
                _state.update { it.copy(dialogBankName = event.value) }
            }

            is ProfilePaymentMethodEvent.OnNewCardNumberChange -> {
                _state.update { it.copy(dialogCardNumber = event.value) }
            }

            is ProfilePaymentMethodEvent.OnNewIsSelectedCardChange -> {
                _state.update { it.copy(dialogIsSelectedCard = event.value) }
            }

            ProfilePaymentMethodEvent.OnOpenDialogClick -> {
                _state.update { it.copy(isDialogOpen = true) }
            }

            ProfilePaymentMethodEvent.OnCloseDialogClick -> {
                _state.update { it.copy(isDialogOpen = false) }
            }

            is ProfilePaymentMethodEvent.OnRemovePaymentMethod -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    removePaymentMethodUseCase(event.methodId)
                }
            }
        }
    }

    override fun mapBaseError(message: String): ProfilePaymentMethodAction {
        return ProfilePaymentMethodAction.OnError(message)
    }
}