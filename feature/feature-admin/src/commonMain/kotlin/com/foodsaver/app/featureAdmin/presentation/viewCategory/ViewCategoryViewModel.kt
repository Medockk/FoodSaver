package com.foodsaver.app.featureAdmin.presentation.viewCategory

import com.foodsaver.app.commonModule.presentation.BaseViewModel

class ViewCategoryViewModel: BaseViewModel<ViewCategoryAction>() {

    override fun mapBaseError(message: String): ViewCategoryAction {
        return ViewCategoryAction.OnError(message)
    }
}