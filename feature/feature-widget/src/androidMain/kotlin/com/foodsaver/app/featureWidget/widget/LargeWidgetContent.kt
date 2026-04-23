package com.foodsaver.app.featureWidget.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.foodsaver.app.featureWidget.model.WidgetProductModel
import kotlin.math.roundToInt

@SuppressLint("RestrictedApi")
@Composable
fun LargeWidgetContent(
    products: List<WidgetProductModel>,
    onRefreshClick: Action
) {
    val whiteColor = ColorProvider(Color.White)

    LazyColumn(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .padding(12.dp),
    ) {
        item {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "FoodSaver", style = TextStyle(color = whiteColor, fontSize = 20.sp))
                Spacer(GlanceModifier.defaultWeight())
                Button(
                    text = "Update",
                    onClick = onRefreshClick
                )
            }

            Spacer(GlanceModifier.height(10.dp))
        }

        items(products) { product ->
            Row {
                // TODO Сделать отображение изображения
                Column {
                    Text(text = product.title, style = TextStyle(whiteColor))
                    Spacer(GlanceModifier.height(5.dp))
                    Text(text = product.description, style = TextStyle(whiteColor))
                }
                Spacer(GlanceModifier.defaultWeight())
                Button(
                    text = "Купить за " + product.price.roundToInt().toString(),
                    onClick = {

                    },
                    style = TextStyle(whiteColor)
                )
            }
            Spacer(GlanceModifier.height(10.dp))
        }
    }
}