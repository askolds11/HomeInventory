package com.askolds.homeinventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsDefaults
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsState
import com.askolds.homeinventory.ui.navigation.composables.Navigation
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appBarsState: AppBarsState
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContent {
            val scrollBehavior = AppBarsDefaults.exitAlwaysScrollBehavior(
                bottomBarState = appBarsState.bottomBarState,
                topBarState = appBarsState.topBarState,
                canBottomScroll = { appBarsState.canBottomScroll },
                canTopScroll = { appBarsState.canTopScroll }
            )
            val appBarsObject = AppBarsObject(appBarsState, scrollBehavior)
            HomeInventoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(appBarsObject = appBarsObject)
                }
            }
        }
    }
}