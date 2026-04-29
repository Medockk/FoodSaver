package com.foodsaver.app.featureWidget.utils

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.action.ActionParameters

object FoodSaverWidgetKeys {

    val productsJsonKey = stringPreferencesKey("products_json_key")
    val errorKey = stringPreferencesKey("product_error_key")

    val actionWidgetType = ActionParameters.Key<String>("widget_type")
}