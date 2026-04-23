package com.foodsaver.app.featureWidget.widget.callback

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.foodsaver.app.featureWidget.data.AndroidWidgetSyncWorker
import com.foodsaver.app.featureWidget.utils.FoodSaverWidgetKeys
import com.foodsaver.app.featureWidget.utils.WidgetSize
import com.foodsaver.app.featureWidget.widget.FoodSaverWidget

class RefreshButtonAction: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val widgetSizeName = parameters[FoodSaverWidgetKeys.actionWidgetType] ?: return
        val widgetSize = try {
            WidgetSize.valueOf(widgetSizeName)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[FoodSaverWidgetKeys.productsJsonKey] = "On Click!\nStart synchronous work"
        }
        FoodSaverWidget(widgetSize).update(context, glanceId)

        val request = OneTimeWorkRequestBuilder<AndroidWidgetSyncWorker>()
            .setInputData(
                inputData = workDataOf(
                    AndroidWidgetSyncWorker.WIDGET_TYPE_KEY to widgetSizeName
                )
            )
            .setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED
                )
            )
        WorkManager.getInstance(context)
            .enqueue(request.build())
    }
}