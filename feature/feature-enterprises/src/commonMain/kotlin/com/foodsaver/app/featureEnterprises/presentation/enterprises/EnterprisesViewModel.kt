@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.foodsaver.app.featureEnterprises.presentation.enterprises

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.commonModule.utils.image.ImageCompressor
import com.foodsaver.app.coreLocation.domain.model.LocationModel
import com.foodsaver.app.coreLocation.domain.repository.LocationService
import com.foodsaver.app.featureEnterprises.domain.model.EnterpriseImagesModel
import com.foodsaver.app.featureEnterprises.domain.model.UploadEnterpriseImageModel
import com.foodsaver.app.featureEnterprises.domain.model.UserLocationModel
import com.foodsaver.app.featureEnterprises.domain.repository.EnterprisesRepository
import com.foodsaver.app.featureEnterprises.domain.usecase.UploadEnterpriseImageUseCase
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesAction.OnError
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesAction.OnSetEnterpriseIcon
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesAction.OnUpdateUserLocation
import com.foodsaver.app.featureEnterprises.presentation.enterprises.EnterprisesAction.OnZoom
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class EnterprisesViewModel(
    private val enterprisesRepository: EnterprisesRepository,
    private val locationService: LocationService,
    private val uploadEnterpriseImageUseCase: UploadEnterpriseImageUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<EnterprisesAction>() {

    override val baseChannel: Channel<EnterprisesAction> = Channel()
    override val channel: Flow<EnterprisesAction> = baseChannel.receiveAsFlow()

//    private val navArgs = savedStateHandle.toRoute<Route.MainGraph.MapScreen>()

    private var shouldZoomMap = true
    private val mapKitControllerDeferred = CompletableDeferred<Unit>()

    private val _coords = MutableStateFlow<LocationModel?>(null)

    private val _state = MutableStateFlow(EnterprisesState())
    val state = _state.asStateFlow()

    private var enterpriseImageJob: Job? = null

    init {
//        val enterpriseId = navArgs.enterpriseId
//        if (enterpriseId != null) {
//            shouldZoomMap = false
//            getEnterpriseById(enterpriseId)
//        }
    }

    private fun getEnterpriseById(enterpriseId: String) {
        viewModelScope.launch(Dispatchers.InputOutput) {
            mapKitControllerDeferred.await()

            enterprisesRepository.getEnterpriseById(enterpriseId)
                .onSuccess { enterprise ->
                    enterprise?.let { enterprise ->
                        _state.update {
                            it.copy(
                                selectedEnterprise = enterprise,
                            )
                        }

                        shouldZoomMap = false
                        baseChannel.send(
                            element = OnZoom(
                                latitude = enterprise.latitude,
                                longitude = enterprise.longitude
                            )
                        )

                        enterpriseImageJob = getEnterpriseImageUrls(enterpriseId)

                    }
                }
        }
    }

    private fun getEnterpriseImageUrls(enterpriseId: String): Job {
        return viewModelScope.launch(Dispatchers.InputOutput) {
            enterprisesRepository.getEnterpriseImageUrls(enterpriseId)
                .onSuccess { enterpriseImagesModels ->
                    _state.update {
                        it.copy(
                            selectedEnterpriseImagesModel = enterpriseImagesModels
                        )
                    }
                }
        }
    }

    fun onEvent(event: EnterprisesEvent) {
        when (event) {
            is EnterprisesEvent.OnCameraPositionChange -> {
                _state.update { it.copy(cameraPositionModel = event.cameraPosition) }
            }

            is EnterprisesEvent.OnEnterpriseMapIconClick -> {

                if (_state.value.selectedEnterprise?.id == event.enterprise.id) return

                _state.update {
                    it.copy(
                        selectedEnterprise = event.enterprise,
                        selectedEnterpriseImagesModel = emptyList()
                    )
                }
                enterpriseImageJob?.cancel()
                enterpriseImageJob = getEnterpriseImageUrls(event.enterprise.id)
            }

            is EnterprisesEvent.OnPhotoPickerLauncherChange -> {
                _state.update { it.copy(isPickerLauncherOpen = event.value) }
            }

            is EnterprisesEvent.OnSelectImage -> {

                viewModelScope.launch(Dispatchers.InputOutput) {

                    val image = async {
                        withContext(Dispatchers.Default) {
                            var newImage = event.image
                            var quality = 95

                            do {
                                newImage =
                                    ImageCompressor.compress(newImage, quality, event.exifData)
                                quality -= 5
                            } while (!ImageCompressor.isImageSizeValid(newImage) && quality > 20)

                            return@withContext newImage
                        }
                    }

                    _state.update { it.copy(isPickerLauncherOpen = false) }

                    val uploadEnterpriseImageModel = UploadEnterpriseImageModel(
                        image = image.await(),
                        mimeType = event.mimeType ?: "image/jpeg",
                        enterpriseId = _state.value.selectedEnterprise!!.id
                    )

                    uploadEnterpriseImageUseCase(uploadEnterpriseImageModel)
                        .onSuccess { url ->
                            url?.let {
                                val selectedEnterpriseImageModel = EnterpriseImagesModel(url)
                                _state.update {
                                    it.copy(
                                        selectedEnterpriseImagesModel = it.selectedEnterpriseImagesModel +
                                                selectedEnterpriseImageModel
                                    )
                                }
                            }
                        }.onFailure {
                            sendError(it)
                        }
                }
            }

            EnterprisesEvent.OnCloseEnterpriseSheet -> {
                _state.update {
                    it.copy(
                        selectedEnterprise = null,
                        selectedEnterpriseImagesModel = emptyList()
                    )
                }
            }

            EnterprisesEvent.OnFindUserClick -> {
                _coords.value?.let {
                    baseChannel.trySend(
                        OnZoom(
                            it.latitude,
                            it.longitude,
                            _state.value.cameraPositionModel?.zoom ?: 17.5f
                        )
                    )

                }
            }

            EnterprisesEvent.OnMapKitControllerReady -> {
                mapKitControllerDeferred.complete(Unit)
                getCurrentLocation()
                getNearestEnterprises()
            }
        }
    }

    private fun getNearestEnterprises() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            _coords
                .filterNotNull()
                .debounce(1500L)
                .distinctUntilChanged { old, new ->
                    calculateDistance(old, new) < 15 // 15 meters
                }
                .flatMapLatest { location ->
                    val userLocation = UserLocationModel(location.latitude, location.longitude)
                    flow {
                        emit(enterprisesRepository.getNearestEnterprises(userLocation))
                    }
                }.collect { result ->
                    result.onSuccess { enterprises ->
                        baseChannel.send(OnSetEnterpriseIcon(enterprises))
                    }.onFailure {
                        sendError(it)
                    }
                }
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            locationService.getCurrentLocation()
                .collect { currentLocation ->
                    _coords.update { currentLocation }
                    baseChannel.send(
                        OnUpdateUserLocation(
                            currentLocation.latitude,
                            currentLocation.longitude
                        )
                    )

                    if (shouldZoomMap) {
                        shouldZoomMap = false
                        baseChannel.send(
                            element = OnZoom(
                                currentLocation.latitude,
                                currentLocation.longitude
                            )
                        )
                    }
                }
        }
    }

    private fun calculateDistance(old: LocationModel, new: LocationModel): Double {
        val r = 6371000.0 // The Earth's radius
        val dLat = (new.latitude - old.latitude).toRadians()
        val dLon = (new.longitude - old.longitude).toRadians()
        val a = sin(dLat / 2).pow(2) +
                cos(old.latitude.toRadians()) * cos(new.latitude.toRadians()) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    private fun Double.toRadians(): Double = this * PI / 180.0

    override fun mapBaseError(message: String): EnterprisesAction {
        return OnError(message)
    }
}