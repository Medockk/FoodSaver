package com.foodsaver.app.data.repository.external

internal external object google {
    internal object accounts {
        internal object id {
            fun initialize(config: IdConfiguration)
            fun prompt(callback: (Notification) -> Unit)

        }
    }
}