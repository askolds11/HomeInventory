package com.askolds.homeinventory.featureParameter.ui

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.askolds.homeinventory.featureParameter.ui.parameterList.ParameterListViewModel
import com.askolds.homeinventory.featureParameter.ui.tmep.TempScreen
import com.askolds.homeinventory.featureParameter.ui.tmep.TempViewModel
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.composables.NavigationGraph
import com.askolds.homeinventory.ui.navigation.defaultEnterTransition
import com.askolds.homeinventory.ui.navigation.defaultExitTransition

sealed class NavigationParameters(val route: String, val args: String? = null) {
    data object ParameterSets : NavigationParameters(route = "${NavigationGraph.Parameters.route}/parameterSets")
//    data object Create : NavigationParameters(route = "${NavigationGraph.Thing.route}/create", args = "/{homeId}?parentId={parentId}") {
//        fun getRoute(homeId: Int, parentId: Int? = null): String {
//            val parentString = if (parentId != null) "?parentId=$parentId" else ""
//            return "${route}/$homeId$parentString"
//        }
//    }
}

fun NavGraphBuilder.parametersGraph(
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {
    val appBarsState = appBarsObject.appBarsState
    navigation(startDestination = NavigationParameters.ParameterSets.route, route = NavigationGraph.Parameters.route) {
        composable(
            route = NavigationParameters.ParameterSets.route,
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
            TempScreen(
                hiltViewModel<TempViewModel>(),
                hiltViewModel<ParameterListViewModel>(),
                navController,
                appBarsObject,
            )
        }
//        composable(
//            route = with (NavigationThing.Create) {route + args},
//            arguments = listOf(
//                navArgument("homeId") {type = NavType.IntType},
//                navArgument("parentId") {nullable = true;/* type = NavType.IntType*/}
//            ),
//            enterTransition = { defaultEnterTransition(this) },
//            exitTransition = { defaultExitTransition(this) }
//        ) {
//            appBarsState.ShowAppBars(lockTop = true, lockBottom = true)
//            ThingFormScreen(hiltViewModel<ThingFormViewModel>(), navController)
//        }
    }
}