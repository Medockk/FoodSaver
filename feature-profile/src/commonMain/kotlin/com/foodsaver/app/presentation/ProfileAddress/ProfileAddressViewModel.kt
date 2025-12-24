package com.foodsaver.app.presentation.ProfileAddress

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileAddressViewModel(
    private val getProfileUseCase: GetProfileUseCase
): ViewModel() {

    var state by mutableStateOf(ProfileAddressState())
        private set

    private val _channel = Channel<ProfileAddressAction>()
    val channel = _channel.receiveAsFlow()

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase().collect {
                when (it) {
                    is ApiResult.Error -> {
                        _channel.send(ProfileAddressAction.OnError(it.error.message))
                    }
                    ApiResult.Loading -> {
                        state = state.copy(isLoading = true)
                    }
                    is ApiResult.Success<UserModel> -> {
                        val address = it.data.addresses
                        withContext(Dispatchers.Main) {
                            state = state.copy(
                                addresses = address
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ProfileAddressEvent) {
        when (event) {
            ProfileAddressEvent.OnAddNewAddressClick -> TODO()
        }
    }
}