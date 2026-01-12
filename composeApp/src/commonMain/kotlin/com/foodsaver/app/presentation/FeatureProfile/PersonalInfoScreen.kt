@file:OptIn(ExperimentalMaterial3Api::class)

package com.foodsaver.app.presentation.FeatureProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.CircularAsyncImage
import com.foodsaver.app.common.CircularPrimaryButton
import com.foodsaver.app.common.PrimaryButton
import com.foodsaver.app.common.PrimaryCenterAlignedTopAppBar
import com.foodsaver.app.presentation.FeatureProfile.components.ProfilePersonalInfoField
import com.foodsaver.app.presentation.ProfilePersonalInfo.ProfilePersonalInfoAction
import com.foodsaver.app.presentation.ProfilePersonalInfo.ProfilePersonalInfoEvent
import com.foodsaver.app.presentation.ProfilePersonalInfo.ProfilePersonalInfoState
import com.foodsaver.app.presentation.ProfilePersonalInfo.ProfilePersonalInfoViewModel
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_change_image_icon
import foodsaver.composeapp.generated.resources.profile
import foodsaver.composeapp.generated.resources.profile_bio
import foodsaver.composeapp.generated.resources.profile_email
import foodsaver.composeapp.generated.resources.profile_full_name
import foodsaver.composeapp.generated.resources.profile_phone
import foodsaver.composeapp.generated.resources.save
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfilePersonalInfoScreenRoot(
    navController: NavController,
    viewModel: ProfilePersonalInfoViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is ProfilePersonalInfoAction.OnError -> {
                snackbarHostState.showSnackbar(it.message)
            }
            ProfilePersonalInfoAction.OnSuccessSave -> {
                navController.popBackStack()
            }
        }
    }
    
    ProfilePersonalInfoScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun ProfilePersonalInfoScreen(
    navController: NavController,
    state: ProfilePersonalInfoState,
    onEvent: (ProfilePersonalInfoEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    
    val profilePersonalInfoFields = listOf(
        ProfilePersonalInfoField(
            label = stringResource(Res.string.profile_full_name),
            placeholder = state.profile?.name ?: "",
            value = state.fullName,
            onValueChange = { onEvent(ProfilePersonalInfoEvent.OnFullNameChange(it)) },
        ),
        ProfilePersonalInfoField(
            label = stringResource(Res.string.profile_email),
            placeholder = state.profile?.email ?: "",
            value = state.email,
            onValueChange = { onEvent(ProfilePersonalInfoEvent.OnEmailChange(it)) },
            keyboardType = KeyboardType.Email
        ),
        ProfilePersonalInfoField(
            label = stringResource(Res.string.profile_phone),
            placeholder = state.profile?.phone ?: "",
            value = state.phone,
            onValueChange = { onEvent(ProfilePersonalInfoEvent.OnPhoneChange(it)) },
            keyboardType = KeyboardType.Phone
        )
    )

    if (state.showGallery) {
        GalleryPickerLauncher(
            includeExif = true,
            onPhotosSelected = { photos ->
                photos.firstOrNull()?.let {
                    onEvent(ProfilePersonalInfoEvent.OnChangeImage(
                        it.loadBytes(),
                        it.mimeType
                    ))
                }
                onEvent(ProfilePersonalInfoEvent.OnCloseGallery)
            },
            onError = {
                onEvent(ProfilePersonalInfoEvent.OnCloseGallery)
            },
            onDismiss = {
                onEvent(ProfilePersonalInfoEvent.OnCloseGallery)
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            PrimaryCenterAlignedTopAppBar(
                title = stringResource(Res.string.profile),
                onNavigationButtonClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                state.profile?.let { profile ->
                    item {
                        Box {
                            CircularAsyncImage(
                                imageUrl = profile.photoUrl,
                                maxSize = 130.dp,
                                modifier = Modifier
                                    .fillParentMaxWidth(0.3f)
                            )

                            CircularPrimaryButton(
                                onClick = {
                                    onEvent(ProfilePersonalInfoEvent.OnOpenGallery)
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.BottomEnd)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_change_image_icon),
                                    contentDescription = "Change image",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .size(16.dp)
                                )
                            }
                        }
                    }

                    items(profilePersonalInfoFields) {
                        ProfilePersonalInfoField(
                            profilePersonalInfoField = it,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    item {
                        ProfilePersonalInfoField(
                            profilePersonalInfoField = ProfilePersonalInfoField(
                                label = stringResource(Res.string.profile_bio),
                                placeholder = state.profile?.bio ?: "",
                                value = state.bio,
                                onValueChange = {
                                    onEvent(ProfilePersonalInfoEvent.OnBioChange(it))
                                },
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillParentMaxHeight(0.2f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            PrimaryButton(
                text = stringResource(Res.string.save),
                onClick = {
                    onEvent(ProfilePersonalInfoEvent.OnSave)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp)
            )
        }
    }
}