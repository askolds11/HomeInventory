package com.askolds.homeinventory.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.askolds.homeinventory.ui.navigation.appbars.AppBarState
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsDefaults
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsState
import com.askolds.homeinventory.ui.navigation.composables.BottomBar

@Preview(
    name = "Dark mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4_XL
)
@Preview(
    name = "Light mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4_XL
)
annotation class DarkLightPreviews

@Composable
fun getPreviewAppBarsObject(): AppBarsObject {
    val appBarsObject = AppBarsObject(
        AppBarsState(
            AppBarState(0f, 0f, 0f),
            AppBarState(0f, 0f, 0f)
        ),
        AppBarsDefaults.exitAlwaysScrollBehavior()
    )
    val navBar = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    //80.dp as per material design, stored in an auto generated private class :)
    appBarsObject.appBarsState.SetBottomPadding(padding = 80.dp + navBar)
    return appBarsObject
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PreviewScaffold(
    appBarsObject: AppBarsObject,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                rememberNavController(),
                appBarsObject
            )
        }
    ) {
        content()
    }
}