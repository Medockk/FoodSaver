package com.foodsaver.app.presentation.profileEditProfile

import androidx.compose.ui.graphics.ImageBitmap

data class ProfileEditProfileState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val bio: String = "",
    val avatarUri: String? = null,
    val avatarImageBitmap: ImageBitmap? = null,
    val avatarImageByteArray: Avatar? = null,

    val isLoading: Boolean = false,
    val isGalleryVisible: Boolean = false,
) {
    class Avatar(val bytes: ByteArray)
}
