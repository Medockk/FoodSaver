package com.foodsaver.app.coreLocation.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import com.foodsaver.app.coreLocation.domain.model.LocationModel
import com.foodsaver.app.coreLocation.domain.repository.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

internal actual class LocationServiceImpl(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val context: Context,
) : LocationService {

    @androidx.annotation.RequiresPermission(
        anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION]
    )
    actual override fun getCurrentLocation(): Flow<LocationModel> = channelFlow {

        if (
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
                .setMinUpdateIntervalMillis(2000L)
                .setMinUpdateDistanceMeters(5f)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { lastLocation ->
                        trySend(
                            LocationModel(
                                latitude = lastLocation.latitude,
                                longitude = lastLocation.longitude
                            )
                        )
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}