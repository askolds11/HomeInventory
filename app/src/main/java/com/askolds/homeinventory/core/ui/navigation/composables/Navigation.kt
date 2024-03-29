package com.askolds.homeinventory.core.ui.navigation.composables

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsDefaults
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.recents.SearchScreen
import com.askolds.homeinventory.featureHome.ui.homeGraph
import com.askolds.homeinventory.featureParameter.ui.parametersGraph

// navigation graphs
sealed class NavigationGraph(val route: String) {
    data object Home: NavigationGraph(route = "home")
    data object Parameters: NavigationGraph(route = "parameters")
}

// navigation bar destinations
sealed class NavigationBase(
    val route: String,
    val iconInactive: ImageVector,
    val iconActive: ImageVector,
    @StringRes val resourceId: Int
) {
    data object Home : NavigationBase(
        route = NavigationGraph.Home.route,
        Icons.Outlined.House,
        Icons.Filled.House,
        R.string.homes
    )
    data object Search : NavigationBase(
        route = "recent",
        Icons.Outlined.Search,
        Icons.Filled.Search,
        R.string.search
    )
    data object Parameters : NavigationBase(
        route = NavigationGraph.Parameters.route,
        Icons.Outlined.CheckBox,
        Icons.Filled.CheckBox,
        R.string.parameters
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    appBarsObject: AppBarsObject
) {
    val appBarsState = appBarsObject.appBarsState
    val scrollBehavior = AppBarsDefaults.exitAlwaysScrollBehavior(
        bottomBarState = appBarsState.bottomBarState,
        topBarState = appBarsState.topBarState,
        canBottomScroll = { appBarsState.canBottomScroll },
        canTopScroll = { appBarsState.canTopScroll }
    )
    Scaffold(
        bottomBar = { BottomBar(navController, appBarsObject) },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationBase.Home.route,
            modifier = Modifier
                .fillMaxSize()
        ) {
            homeGraph(navController, appBarsObject)
//            homeThingGraph(navController, appBarsObject)
            parametersGraph(navController, appBarsObject)
            composable(route = NavigationBase.Search.route) {
                SearchScreen(navController)
            }
        }
    }
}