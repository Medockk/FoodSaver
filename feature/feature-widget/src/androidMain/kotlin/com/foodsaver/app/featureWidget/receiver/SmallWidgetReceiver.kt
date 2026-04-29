package com.foodsaver.app.featureWidget.receiver

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.foodsaver.app.featureWidget.utils.WidgetSize
import com.foodsaver.app.featureWidget.widget.FoodSaverWidget


class SmallWidgetReceiver: GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget
        get() = FoodSaverWidget(WidgetSize.Small)
}