package com.foodsaver.app.androidApp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.retain.retain
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.App
import com.foodsaver.app.AppViewModel
import com.foodsaver.app.AuthenticationState
import com.foodsaver.app.navigationModule.Route
import org.koin.android.ext.android.inject
import kotlin.jvm.java

class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null
    private val appViewModel: AppViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
            .setKeepOnScreenCondition {
                val isSplashScreenVisible = appViewModel.authenticationState.value
                println("isSplashScreenVisible $isSplashScreenVisible")
                return@setKeepOnScreenCondition isSplashScreenVisible is AuthenticationState.Loading
            }
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        if (!isLocationAccessGranted()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }

        setContent {
            navController = rememberNavController()

            val navigationUri = intent.data ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                intent.getParcelableExtra(Intent.EXTRA_STREAM)
            }
            var deepLinkAction = retain {
                val extras = intent.extras?.getString("product_id")
                if (extras != null) {
                    { navController: NavController ->
                        navController.navigate(Route.MainGraph.FoodDetailsScreen(extras, "" /*TODO make normal deeplink*/))
                    }
                } else { null }
            }

            App(
                navController = navController!!,
                onHandleDeepLink = deepLinkAction,
                onDeepLinkHandled = { deepLinkAction = null }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        navController?.handleDeepLink(intent)
    }

    private fun isLocationAccessGranted(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}