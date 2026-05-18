package com.foodsaver.app.presentation.profilePersonalInfo

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreProfile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfilePersonalInfoViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : BaseViewModel<ProfilePersonalInfoAction>() {

    private val _state = MutableStateFlow(ProfilePersonalInfoState())
    val state = _state.asStateFlow()


    override val baseChannel: Channel<ProfilePersonalInfoAction> = Channel()
    override val channel = baseChannel.receiveAsFlow()

    init {
        getProfileInfo()
    }

    private fun getProfileInfo() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase.invoke().collectRequest(
                onSuccess = { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            profile = result
                        )
                    }
                }
            )
        }
    }

    override fun mapBaseError(message: String): ProfilePersonalInfoAction {
        return ProfilePersonalInfoAction.OnError(message)
    }
}