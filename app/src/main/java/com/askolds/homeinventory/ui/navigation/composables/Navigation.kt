package com.askolds.homeinventory.ui.navigation.composables

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureHome.ui.homeGraph
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsDefaults
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsViewModel
import com.askolds.homeinventory.ui.recents.RecentsScreen

sealed class NavigationBase(
    val route: String,
    val iconInactive: ImageVector,
    val iconActive: ImageVector,
    @StringRes val resourceId: Int
) {
    data object Home : NavigationBase(
        route = "homes",
        Icons.Outlined.House,
        Icons.Filled.House,
        R.string.homes
    )
    data object Recents : NavigationBase(
        route = "recents",
        Icons.Outlined.Schedule,
        Icons.Filled.Schedule,
        R.string.recents
    )
    //data object Blank : NavigationBase(route = "blank", Icons.Outlined.CheckBoxOutlineBlank, Icons.Filled.CheckBox, "Crash!")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
) {
    val appBarsViewModel: AppBarsViewModel = hiltViewModel()

    val scrollBehavior = AppBarsDefaults.exitAlwaysScrollBehavior(
        bottomBarState = appBarsViewModel.appBarsState.bottomBarState,
        topBarState = appBarsViewModel.appBarsState.topBarState,
        canBottomScroll = { appBarsViewModel.appBarsState.canBottomScroll },
        canTopScroll = { appBarsViewModel.appBarsState.canTopScroll }
    )
    Scaffold(
        topBar = { TopBar(navController, scrollBehavior) },
        floatingActionButton = { FAB(navController) },
        bottomBar = { BottomBar(navController, scrollBehavior) },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)

    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationBase.Home.route,
            modifier = Modifier
                .fillMaxSize()
        ) {
            homeGraph(navController, appBarsViewModel)
            composable(route = NavigationBase.Recents.route) {
                RecentsScreen(navController)
            }
        }
    }
}