package com.foodsaver.app.coreAuth

import com.foodsaver.app.commonModule.utils.uiText.LocalError

class UserNotAuthorizedException: LocalError<Any>, Throwable() {

    override val error: Any = this
}