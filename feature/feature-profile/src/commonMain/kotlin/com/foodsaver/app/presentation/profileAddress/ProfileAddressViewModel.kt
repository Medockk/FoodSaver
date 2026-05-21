package com.foodsaver.app.presentation.profileAddress

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
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
    override val channel = baseChannel.receiveAsFlow()

    init {
        getAddresses()
    }

    private fun getAddresses() = viewModelScope.launch(Dispatchers.InputOutput) {
        readAddressRepository.observeUserAddresses().collectRequest(
            onSuccess = { addresses ->
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
                sendError(errorResponse)
            }
        )
    }

    fun onEvent(event: ProfileAddressEvent) {
//       TODO()
    }

    override fun mapBaseError(message: String): ProfileAddressAction {
        return ProfileAddressAction.OnError(message)
    }
}