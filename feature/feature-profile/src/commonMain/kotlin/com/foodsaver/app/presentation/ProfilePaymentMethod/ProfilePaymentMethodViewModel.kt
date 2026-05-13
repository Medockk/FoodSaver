package com.foodsaver.app.presentation.ProfilePaymentMethod

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfilePaymentMethodViewModel(

) : BaseViewModel<ProfilePaymentMethodAction>() {

    private val _state = MutableStateFlow(ProfilePaymentMethodState())
    val state = _state.asStateFlow()

    override val baseChannel: Channel<ProfilePaymentMethodAction> = Channel()
    override val channel = baseChannel.receiveAsFlow()

    init {
        getPaymentMethods()
    }

    private fun getPaymentMethods() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            TODO()
        }
    }

    fun onEvent(event: ProfilePaymentMethodEvent) {
        when (event) {
            ProfilePaymentMethodEvent.OnAddNewCardClick -> {
                TODO()
            }

            is ProfilePaymentMethodEvent.OnRemovePaymentMethod -> {
                TODO()
            }
        }
    }

    override fun mapBaseError(message: String): ProfilePaymentMethodAction {
        return ProfilePaymentMethodAction.OnError(message)
    }
}