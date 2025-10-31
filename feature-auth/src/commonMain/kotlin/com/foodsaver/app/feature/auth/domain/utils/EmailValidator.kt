package com.foodsaver.app.feature.auth.domain.utils

object EmailValidator {

    private val EMAIL_REGEX = Regex(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    )

    fun validate(email: String?): Boolean {
        val e = email ?: return false
        return EMAIL_REGEX.matchEntire(e) != null
    }
}
