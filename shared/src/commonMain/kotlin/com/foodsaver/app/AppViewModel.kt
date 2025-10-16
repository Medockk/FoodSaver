package com.foodsaver.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.domain.model.Auth.SignInModel
import com.foodsaver.app.domain.repository.ProfileRepository
import com.foodsaver.app.domain.usecase.Auth.SignInUseCase
import com.foodsaver.app.utils.InputOutput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(
    private val signInUseCase: SignInUseCase,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.InputOutput) {
            val result = profileRepository.getAllUsers()
            println(result)
//            val result = signInUseCase(SignInModel("demo1@gmail.com", "qwe1@3QWE"))
//            println(result)
        }
    }
}