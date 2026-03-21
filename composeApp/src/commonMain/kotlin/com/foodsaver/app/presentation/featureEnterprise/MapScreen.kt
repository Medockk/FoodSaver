package com.foodsaver.app.presentation.featureEnterprise

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitVerticalPointerSlopOrCancellation
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.commonModule.utils.image.ExifData
import com.foodsaver.app.commonModule.utils.image.ExifOrientationParser
import com.foodsaver.app.featureEnterprises.domain.model.CameraPositionModel
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesAction
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesEvent
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesEvent.OnCameraPositionChange
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesEvent.OnEnterpriseMapIconClick
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesState
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesViewModel
import com.foodsaver.app.presentation.featureEnterprise.components.EnterpriseBottomSheet
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.utils.ObserveActions
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun EnterpriseScreenRoot(
    navController: NavController,
    viewModel: EnterprisesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var mapKitController by remember { mutableStateOf<MapKitController?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is EnterprisesAction.OnError -> {
                snackbarHostState.showSnackbar(it.message)
            }

            is EnterprisesAction.OnZoom -> {
                mapKitController?.zoomTo(it.latitude, it.longitude, it.zoom)
            }

            is EnterprisesAction.OnSetEnterpriseIcon -> {
                val mapKitObject = MapKitObjectFactory.createMapKitObject(MapKitObjectType.ENTERPRISE_ICON)
                it.enterprises.forEach { enterprise ->
                    mapKitController?.setPoint(
                        id = enterprise.id,
                        latitude = enterprise.latitude,
                        longitude = enterprise.longitude,
                        mapKitObject = mapKitObject,
                        onClick = {
                            viewModel.onEvent(OnEnterpriseMapIconClick(enterprise))
                            true
                        }
                    )
                }
            }

            is EnterprisesAction.OnUpdateUserLocation -> {
                mapKitController?.setPoint(
                    id = "user_location",
                    latitude = it.latitude,
                    longitude = it.longitude,
                    mapKitObject = MapKitObjectFactory.createMapKitObject(MapKitObjectType.USER_ICON),
                    onClick = null
                )
            }
        }
    }

    EnterprisesScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onMapKitControllerReady = {
            mapKitController = it
            viewModel.onEvent(EnterprisesEvent.OnMapKitControllerReady)
        },
        onMapKitEvent = { event ->
            when (event) {
                is MapKitEvent.OnCameraChanged -> {
                    val cameraPosition = CameraPositionModel(
                        latitude = event.latitude,
                        longitude = event.longitude,
                        zoom = event.zoom
                    )
                    viewModel.onEvent(OnCameraPositionChange(cameraPosition))
                }

                MapKitEvent.OnLocationAccessDenied -> {

                }
            }
        },
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun EnterprisesScreen(
    state: EnterprisesState,
    snackbarHostState: SnackbarHostState,
    onMapKitControllerReady: (MapKitController) -> Unit,
    onMapKitEvent: (MapKitEvent) -> Unit,
    onEvent: (EnterprisesEvent) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val velocityTracker = remember { VelocityTracker() }
    var sheetHeight by remember { mutableStateOf(0f) }
    val sheetOffsetAnimator = remember { Animatable(0f) }

    if (state.isPickerLauncherOpen) {
        GalleryPickerLauncher(
            onPhotosSelected = { results ->
                if (results.isNotEmpty()) {
                    results.firstOrNull()?.let { image ->
                        val exif = image.exif
                        val exifData = exif?.let {
                            ExifData(
                                orientation = ExifOrientationParser.parseStringOrientation(it.orientation)
                            )
                        }
                        onEvent(
                            EnterprisesEvent.OnSelectImage(
                                image = image.loadBytes(),
                                mimeType = image.mimeType,
                                exifData = exifData
                            )
                        )
                    }
                }
            },
            onError = {
                onEvent(EnterprisesEvent.OnPhotoPickerLauncherChange(false))
            },
            onDismiss = {
                onEvent(EnterprisesEvent.OnPhotoPickerLauncherChange(false))
            },
            includeExif = true
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { _ ->

        Box {

            Box(Modifier.fillMaxSize()) {
                MapKit.DrawMap(
                    onMapKitControllerReady = onMapKitControllerReady,
                    onEvent = onMapKitEvent,
                    initialPosition = state.cameraPositionModel
                )

                FloatingActionButton(
                    onClick = {
                        onEvent(EnterprisesEvent.OnFindUserClick)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                ) {}
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(tween())
                    .onGloballyPositioned {
                        sheetHeight = it.size.height.toFloat()
                    }
                    .offset {
                        IntOffset(0, sheetOffsetAnimator.value.roundToInt())
                    }
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FoodSaverTheme.colorScheme.background
                )
            ) {
                state.selectedEnterprise?.let { selectedEnterprise ->
                    var isScrollInProgress by remember { mutableStateOf(false) }
                    EnterpriseBottomSheet(
                        enterpriseModel = selectedEnterprise,
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val pointerId = awaitFirstDown().id
                                        velocityTracker.resetTracking()

                                        val drag = awaitVerticalPointerSlopOrCancellation(
                                            pointerId = pointerId,
                                            pointerType = PointerType.Touch
                                        ) { change, overSlop ->
                                            isScrollInProgress = true
                                            if (overSlop > 0f && isScrollInProgress) {
                                                coroutineScope.launch {
                                                    val snapValue =
                                                        sheetOffsetAnimator.value + overSlop
                                                    sheetOffsetAnimator.snapTo(snapValue)
                                                }
                                                velocityTracker.addPosition(
                                                    change.uptimeMillis,
                                                    change.position
                                                )
                                                change.consume()
                                                isScrollInProgress = false
                                            }
                                        }

                                        drag?.let {
                                            this@awaitPointerEventScope.verticalDrag(drag.id) { change ->
                                                val dragValue = change.positionChange().y

                                                coroutineScope.launch {
                                                    val snapValue =
                                                        (sheetOffsetAnimator.value + dragValue)
                                                            .coerceAtLeast(0f)
                                                    sheetOffsetAnimator.snapTo(snapValue)
                                                }
                                                velocityTracker.addPosition(
                                                    change.uptimeMillis,
                                                    change.position
                                                )
                                                change.consume()
                                            }

                                            val velocity = velocityTracker.calculateVelocity().y
                                            if (sheetOffsetAnimator.value > sheetHeight * 0.4f || velocity > 500f) {
                                                coroutineScope.launch {
                                                    sheetOffsetAnimator.animateTo(sheetHeight)
                                                    onEvent(EnterprisesEvent.OnCloseEnterpriseSheet)
                                                    sheetOffsetAnimator.snapTo(0f)
                                                }
                                            } else {
                                                coroutineScope.launch {
                                                    sheetOffsetAnimator.animateTo(0f, spring())
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                        images = state.selectedEnterpriseImagesModel,
                        onAddEnterpriseClick = {
                            onEvent(EnterprisesEvent.OnPhotoPickerLauncherChange(true))
                        },
                        onMenuButtonClick = { TODO() }
                    )
                }
            }
        }
    }
}