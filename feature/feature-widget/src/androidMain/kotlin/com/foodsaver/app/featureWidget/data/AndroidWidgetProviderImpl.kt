package com.foodsaver.app.featureWidget.data

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.foodsaver.app.featureWidget.domain.WidgetProvider
import com.foodsaver.app.featureWidget.receiver.MediumWidgetReceiver

internal class AndroidWidgetProviderImpl(
    private val context: Context
): WidgetProvider {

    override val isPinSupport: Boolean =
        AppWidgetManager.getInstance(context).isRequestPinAppWidgetSupported

    override suspend fun requestPinWidget() {
        if (!isPinSupport) return // if not supporting return // or throw?

        val manager = AppWidgetManager.getInstance(context)
        val provider = ComponentName(context, MediumWidgetReceiver::class.java)

        val requestCode = 1001
        val intent = Intent(context, MediumWidgetReceiver::class.java)
        intent.putExtra("requestCode", requestCode)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(context, MediumWidgetReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        manager.requestPinAppWidget(provider, null, pendingIntent)
    }
}