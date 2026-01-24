package com.foodsaver.app.presentation.ProfileAddress

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.ApiResult.onFailure
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreAddress.domain.model.AddAddressModel
import com.foodsaver.app.coreAddress.domain.repository.ReadAddressRepository
import com.foodsaver.app.coreAddress.domain.usecase.AddAddressUseCase
import com.foodsaver.app.coreAddress.domain.usecase.RemoveAddressUseCase
import com.foodsaver.app.coreAddress.domain.usecase.SetCurrentAddressUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileAddressViewModel(
    private val readAddressRepository: ReadAddressRepository,
    private val addAddressUseCase: AddAddressUseCase,
    private val removeAddressUseCase: RemoveAddressUseCase,
    private val setCurrentAddressUseCase: SetCurrentAddressUseCase
) : BaseViewModel<ProfileAddressAction>() {

    private val _state = MutableStateFlow(ProfileAddressState())
    val state = _state.asStateFlow()

    override val baseChannel = Channel<ProfileAddressAction>()
    val channel = baseChannel.receiveAsFlow()

    init {
        getAddresses()
    }

    private fun getAddresses() = viewModelScope.launch(Dispatchers.InputOutput) {
        readAddressRepository.getAddresses().collectRequest(
            onSuccess = { addresses ->
                println("ADDRESSES IS $addresses")
                _state.update { it.copy(
                    addresses = addresses,
                    isLoading = false
                ) }
            },
            onLoading = {
                _state.update {
                    it.copy(isLoading = true)
                }
            },
            onError = { errorResponse ->
                _state.update { it.copy(isLoading = false) }
                sendError(errorResponse.message)
            }
        )
    }

    fun onEvent(event: ProfileAddressEvent) {
        when (event) {
            ProfileAddressEvent.OnAddNewAddressClick -> {
                _state.update { it.copy(shouldShowDialog = true) }
            }

            ProfileAddressEvent.OnCloseDialog -> {
                _state.update { it.copy(shouldShowDialog = false) }
            }

            is ProfileAddressEvent.OnDialogAddressNameChange -> {
                _state.update { it.copy(dialogAddressName = event.value) }
            }

            is ProfileAddressEvent.OnDialogAddressValueChange -> {
                _state.update { it.copy(dialogAddressValue = event.value) }
            }

            ProfileAddressEvent.OnSaveAddress -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    addAddressUseCase.invoke(AddAddressModel(
                        name = _state.value.dialogAddressName,
                        address = _state.value.dialogAddressValue,
                        isCurrentAddress = _state.value.dialogIsCurrentAddress
                    )).onFailure {
                        sendError(it.message)
                    }
                }

                _state.update { it.copy(shouldShowDialog = false) }
            }

            is ProfileAddressEvent.OnDialogIsCurrentAddressChange -> {
                _state.update { it.copy(dialogIsCurrentAddress = event.value) }
            }
        }
    }

    override fun mapBaseError(message: String): ProfileAddressAction {
        return ProfileAddressAction.OnError(message)
    }
}