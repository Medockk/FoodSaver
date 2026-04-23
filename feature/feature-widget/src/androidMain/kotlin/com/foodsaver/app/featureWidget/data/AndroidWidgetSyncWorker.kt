package com.foodsaver.app.featureWidget.data

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.featureWidget.utils.FoodSaverWidgetKeys
import com.foodsaver.app.featureWidget.utils.WidgetSize
import com.foodsaver.app.featureWidget.utils.WidgetUtils
import com.foodsaver.app.featureWidget.widget.FoodSaverWidget
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

class AndroidWidgetSyncWorker(
    private val appContext: Context,
    private val params: WorkerParameters,
    private val repository: ReadProductRepository,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val products = repository.getProducts(0, 10)
            products.onSuccess { products ->
                // parse list of ProductModel to list of the WidgetProductModel
                val widgetModels = products.map { dto ->
                    WidgetUtils.toWidgetModel(dto)
                }
                // serialize to json string
                val json = Json.encodeToString(widgetModels)
                updateAllWidgets { prefs ->
                    // updating widget preferences
                    prefs[FoodSaverWidgetKeys.productsJsonKey] = json
                }

            }.onFailure { error ->
                updateAllWidgets { prefs ->
                    prefs[FoodSaverWidgetKeys.errorKey] = error.uiText.asString()
                }

                return Result.failure()
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private suspend fun updateAllWidgets(onUpdate: suspend (prefs: MutablePreferences) -> Unit) {
        val manager = GlanceAppWidgetManager(appContext)
        val glanceIds = manager.getGlanceIds(FoodSaverWidget::class.java)
        val widgetSizeName = inputData.getString(WIDGET_TYPE_KEY)
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(appContext, glanceId, onUpdate)
        }

        if (widgetSizeName != null) {
            val widgetSize = WidgetSize.valueOf(widgetSizeName)
            FoodSaverWidget(widgetSize).updateAll(appContext)
        } else {
            WidgetSize.entries.forEach { size ->
                FoodSaverWidget(size).updateAll(appContext)
            }
        }
    }

    companion object {
        fun doPeriodicRequest(appContext: Context) {
            val request = PeriodicWorkRequestBuilder<AndroidWidgetSyncWorker>(15, TimeUnit.MINUTES)
                .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
                .build()

            WorkManager.getInstance(appContext)
                .enqueueUniquePeriodicWork(
                    "FoodSaverWidgetUpdate",
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
        }

        const val WIDGET_TYPE_KEY = "WIDGET_TYPE_KEY"
    }
}