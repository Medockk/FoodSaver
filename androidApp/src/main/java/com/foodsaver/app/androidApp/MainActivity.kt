package com.foodsaver.app.androidApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.foodsaver.app.App
import com.foodsaver.app.utils.MapKit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
//        MapKit.init(this.applicationContext)
//        MapKit.onStart()

        setContent {
            App()
        }
    }

    override fun onStart() {
        super.onStart()
//        MapKit.onStart()
    }

    override fun onStop() {
//        MapKit.onStop()
        super.onStop()
    }
}