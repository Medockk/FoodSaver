package com.foodsaver.app.featureWidget.widget

import android.content.Context
import androidx.compose.runtime.remember
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.foodsaver.app.featureWidget.utils.FoodSaverWidgetKeys
import com.foodsaver.app.featureWidget.utils.WidgetSize
import com.foodsaver.app.featureWidget.utils.WidgetUtils
import com.foodsaver.app.featureWidget.widget.callback.RefreshButtonAction

class FoodSaverWidget(private val type: WidgetSize): GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val currentState = currentState<Preferences>()
            val jsonProducts = currentState[FoodSaverWidgetKeys.productsJsonKey] ?: ""
            val products = remember(jsonProducts) {
                WidgetUtils.fromJson(jsonProducts)
            }
            val actionRefresh = actionRunCallback<RefreshButtonAction>(
                parameters = actionParametersOf(
                    when (type) {
                        WidgetSize.Small -> FoodSaverWidgetKeys.actionWidgetType to WidgetSize.Small.name
                        WidgetSize.Medium -> FoodSaverWidgetKeys.actionWidgetType to WidgetSize.Medium.name
                        WidgetSize.Large -> FoodSaverWidgetKeys.actionWidgetType to WidgetSize.Large.name
                    }
                )
            )

            when (type) {
                WidgetSize.Small -> SmallWidgetContent(products, actionRefresh)
                WidgetSize.Medium -> MediumWidgetContent(products, actionRefresh)
                WidgetSize.Large -> LargeWidgetContent(products, actionRefresh)
            }
        }
    }
}