package com.foodsaver.app.presentation.featureProfile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.common.textField.PrimaryTextFieldState
import com.foodsaver.app.common.textField.fieldItem.TextFieldItem
import com.foodsaver.app.common.textField.fieldItem.TextFieldItemState
import com.foodsaver.app.common.topBar.PrimaryTopBar
import com.foodsaver.app.presentation.featureProfile.component.EditProfileImage
import com.foodsaver.app.presentation.profileEditProfile.ProfileEditProfileEvent
import com.foodsaver.app.presentation.profileEditProfile.ProfileEditProfileState
import com.foodsaver.app.presentation.profileEditProfile.ProfileEditProfileViewModel
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.edit_profile
import foodsaver.composeapp.generated.resources.profile_bio
import foodsaver.composeapp.generated.resources.profile_email
import foodsaver.composeapp.generated.resources.profile_full_name
import foodsaver.composeapp.generated.resources.profile_phone_number
import foodsaver.composeapp.generated.resources.save
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.domain.extensions.loadImageBitmap
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileScreenRoot(
    navController: NavController,
    viewModel: ProfileEditProfileViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    EditProfileScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditProfileScreenPreview() {
    LocalFoodSaverThemeComposition {
        Scaffold { padding ->
            Box(Modifier.padding(padding)) {
                EditProfileScreen(
                    navController = rememberNavController(),
                    state = ProfileEditProfileState(),
                    onEvent = { TODO() }
                )
            }
        }
    }
}

@Composable
private fun EditProfileScreen(
    navController: NavController,
    state: ProfileEditProfileState,
    onEvent: (ProfileEditProfileEvent) -> Unit,
) {

    val fields = listOf(
        TextFieldItemState(
            state = PrimaryTextFieldState(
                value = state.fullName,
                onValueChange = { onEvent(ProfileEditProfileEvent.OnFullNameChange(it)) },
                placeholder = "",
                maxLines = 1
            ),
            title = Res.string.profile_full_name
        ),
        TextFieldItemState(
            state = PrimaryTextFieldState(
                value = state.email,
                onValueChange = { onEvent(ProfileEditProfileEvent.OnEmailChange(it)) },
                placeholder = "",
                maxLines = 1
            ),
            title = Res.string.profile_email
        ),
        TextFieldItemState(
            state = PrimaryTextFieldState(
                value = state.phone,
                onValueChange = { onEvent(ProfileEditProfileEvent.OnPhoneChange(it)) },
                placeholder = "",
                maxLines = 1,
                keyboardType = KeyboardType.Number
            ),
            title = Res.string.profile_phone_number
        ),
    )
    val bioField = TextFieldItemState(
        state = PrimaryTextFieldState(
            value = state.bio,
            onValueChange = { onEvent(ProfileEditProfileEvent.OnBioChange(it)) },
            placeholder = "",
            maxLines = 5
        ),
        title = Res.string.profile_bio
    )

    if (state.isGalleryVisible) {
        GalleryPickerLauncher(
            onPhotosSelected = { photos ->
                photos.firstOrNull()?.let { photo ->
                    val bitmap = photo.loadImageBitmap()
                    val byteArray = photo.loadBytes()
                    bitmap?.let { bitmap ->
                        onEvent(ProfileEditProfileEvent.OnAvatarChange(bitmap, byteArray))
                    }
                }
            },
            onError = {
                onEvent(ProfileEditProfileEvent.OnImagePicker(false))
            },
            onDismiss = {
                onEvent(ProfileEditProfileEvent.OnImagePicker(false))
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = FoodSaverTheme.colorScheme.background,
        topBar = {
            PrimaryTopBar(
                title = stringResource(Res.string.edit_profile),
                onNavigationClick = {
                    navController.navigateUp()
                }
            )
        },
        modifier = Modifier.imePadding()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentPadding = paddingValues
        ) {

            // image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EditProfileImage(
                        image = state.avatarImageBitmap ?: state.avatarUri ?: "",
                        onChangeImageClick = {
                            onEvent(ProfileEditProfileEvent.OnImagePicker(true))
                        },
                        modifier = Modifier
                            .size(130.dp)
                    )
                }
            }

            // spacer
            item {
                Spacer(Modifier.height(30.dp))
            }

            items(fields) { field ->
                TextFieldItem(
                    state = field,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            item {
                TextFieldItem(
                    state = bioField,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(100.dp)
                )
                Spacer(Modifier.height(10.dp))
            }

            // save button
            item {
                PrimaryButton(
                    onClick = {
                        onEvent(ProfileEditProfileEvent.OnSave)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(Res.string.save).uppercase()
                )
            }
        }
    }
}