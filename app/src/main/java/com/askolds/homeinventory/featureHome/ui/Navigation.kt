package com.askolds.homeinventory.featureHome.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.askolds.homeinventory.featureHome.ui.formScreen.HomeFormScreen
import com.askolds.homeinventory.featureHome.ui.formScreen.HomeFormViewModel
import com.askolds.homeinventory.featureHome.ui.homeScreen.HomeScreen
import com.askolds.homeinventory.featureHome.ui.homeScreen.HomeViewModel
import com.askolds.homeinventory.featureHome.ui.listScreen.HomeListScreen
import com.askolds.homeinventory.featureHome.ui.listScreen.HomeListViewModel
import com.askolds.homeinventory.featureThing.ui.homeThingGraph
import com.askolds.homeinventory.featureThing.ui.list.ThingListViewModel
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.composables.NavigationGraph
import com.askolds.homeinventory.core.ui.navigation.defaultEnterTransition
import com.askolds.homeinventory.core.ui.navigation.defaultExitTransition

// Home navigation

sealed class NavigationHome(val route: String, val args: String? = null) {
    data object List : NavigationHome(route = "${NavigationGraph.Home.route}/list")
    data object Create : NavigationHome(route = "${NavigationGraph.Home.route}/create")
    data object Edit : NavigationHome(route = "${NavigationGraph.Home.route}/edit", args = "/{homeId}") {
        fun getRoute(homeId: Int) = "${this.route}/$homeId"
    }
    data object Home : NavigationHome(route = NavigationGraph.Home.route, args = "/{homeId}") {
        fun getRoute(homeId: Int) = "${this.route}/$homeId"
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
    val appBarsState = appBarsObject.appBarsState
    navigation(startDestination = NavigationHome.List.route, route = NavigationGraph.Home.route) {
        homeThingGraph(navController, appBarsObject)

        composable(
            route = NavigationHome.List.route,
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {

            appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
            HomeListScreen(
                hiltViewModel<HomeListViewModel>(),
                navController,
                appBarsObject
            )
        }
        composable(
            route = NavigationHome.Create.route,
           enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = true)
            HomeFormScreen(hiltViewModel<HomeFormViewModel>(), appBarsObject, navController)
        }
        composable(
            route = with (NavigationHome.Edit) { route + args },
            arguments = listOf(navArgument("homeId") { type = NavType.IntType }),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = true)
            HomeFormScreen(hiltViewModel<HomeFormViewModel>(), appBarsObject, navController)
        }
        composable(
            route = with (NavigationHome.Home) { route + args },
            arguments = listOf(navArgument("homeId") { type = NavType.IntType }),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
            HomeScreen(
                hiltViewModel<HomeViewModel>(),
                hiltViewModel<ThingListViewModel>(),
                navController,
                appBarsObject
            )
        }
    }
}