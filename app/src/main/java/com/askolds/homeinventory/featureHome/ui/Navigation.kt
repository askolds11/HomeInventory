package com.askolds.homeinventory.featureHome.ui

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.askolds.homeinventory.featureHome.ui.create.HomeCreateScreen
import com.askolds.homeinventory.featureHome.ui.create.HomeCreateViewModel
import com.askolds.homeinventory.featureHome.ui.home.HomeScreen
import com.askolds.homeinventory.featureHome.ui.home.HomeViewModel
import com.askolds.homeinventory.featureHome.ui.list.HomeListScreen
import com.askolds.homeinventory.featureHome.ui.list.HomeListViewModel
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.composables.NavigationBase
import com.askolds.homeinventory.ui.navigation.defaultEnterTransition
import com.askolds.homeinventory.ui.navigation.defaultExitTransition

sealed class NavigationHome(val route: String, val args: String? = null) {
    data object List : NavigationHome(route = "${NavigationBase.Home.route}/list")
    data object Create : NavigationHome(route = "${NavigationBase.Home.route}/create")
    data object Home : NavigationHome(route = "${NavigationBase.Home.route}/home", args = "/{homeId}") {
        fun getRoute(homeId: Int) = "${this.route}/$homeId"
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {
    val appBarsState = appBarsObject.appBarsState
    navigation(startDestination = NavigationHome.List.route, route = NavigationBase.Home.route) {
        composable(
            route = NavigationHome.List.route,
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {

            appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
            HomeListScreen(
                hiltViewModel<HomeListViewModel>(key = "test"),
                navController,
                appBarsObject,
                modifier
            )
        }
        composable(
            route = NavigationHome.Create.route,
           enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = true)
            HomeCreateScreen(hiltViewModel<HomeCreateViewModel>(), navController)
        }
        composable(
            route = NavigationHome.Home.route + NavigationHome.Home.args,
            arguments = listOf(navArgument("homeId") { type = NavType.IntType }),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.HideAppBars(lockTop = true, lockBottom = true)
            HomeScreen(hiltViewModel<HomeViewModel>(), navController)
        }
    }
}