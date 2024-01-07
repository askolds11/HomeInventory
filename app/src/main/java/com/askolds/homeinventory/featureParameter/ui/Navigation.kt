package com.askolds.homeinventory.featureParameter.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.askolds.homeinventory.featureParameter.ui.parameterScreen.ParameterScreen
import com.askolds.homeinventory.featureParameter.ui.parameterScreen.ParameterViewModel
import com.askolds.homeinventory.featureParameter.ui.parameterFormScreen.ParameterFormScreen
import com.askolds.homeinventory.featureParameter.ui.parameterFormScreen.ParameterFormViewModel
import com.askolds.homeinventory.featureParameter.ui.parameterListScreen.ParameterListScreen
import com.askolds.homeinventory.featureParameter.ui.parameterListScreen.ParameterListViewModel
import com.askolds.homeinventory.featureParameter.ui.parameterSet.ParameterSetScreen
import com.askolds.homeinventory.featureParameter.ui.parameterSet.ParameterSetViewModel
import com.askolds.homeinventory.featureParameter.ui.parameterSetForm.ParameterSetFormScreen
import com.askolds.homeinventory.featureParameter.ui.parameterSetForm.ParameterSetFormViewModel
import com.askolds.homeinventory.featureParameter.ui.parameterSetListScreen.ParameterSetListScreen
import com.askolds.homeinventory.featureParameter.ui.parameterSetListScreen.ParameterSetListViewModel
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.composables.NavigationGraph
import com.askolds.homeinventory.core.ui.navigation.defaultEnterTransition
import com.askolds.homeinventory.core.ui.navigation.defaultExitTransition

sealed class NavigationParameters(val route: String, val args: String? = null) {
    // region Parameter
    data object ParameterCreate : NavigationParameters(route = "${NavigationGraph.Parameters.route}/parameter/create")
    data object ParameterEdit : NavigationParameters(route = "${NavigationGraph.Parameters.route}/parameter/edit", args = "/{parameterId}") {
        fun getRoute(parameterId: Int) = "${this.route}/$parameterId"
    }
    data object ParameterList : NavigationParameters(route = "${NavigationGraph.Parameters.route}/parameter/list")
    data object ParameterView : NavigationParameters(route = "${NavigationGraph.Parameters.route}/parameter/view", args = "/{parameterId}") {
        fun getRoute(parameterId: Int) = "${this.route}/$parameterId"
    }
    // endregion Parameter
    // region Parameter Set
    data object ParameterSetView : NavigationParameters(route = "${NavigationGraph.Parameters.route}/parameterSet/view", args = "/{parameterSetId}") {
        fun getRoute(parameterSetId: Int) = "${this.route}/$parameterSetId"
    }
    data object ParameterSetList : NavigationParameters(route = "${NavigationGraph.Parameters.route}/parameterSet/list")
    data object ParameterSetCreate : NavigationParameters(route = "${NavigationGraph.Parameters.route}/parameterSet/create")
    data object ParameterSetEdit : NavigationParameters(route = "${NavigationGraph.Parameters.route}/parameterSet/edit", args = "/{parameterSetId}") {
        fun getRoute(parameterSetId: Int) = "${this.route}/$parameterSetId"
    }
    // endregion Parameter Set
}

fun NavGraphBuilder.parametersGraph(
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
    val appBarsState = appBarsObject.appBarsState
    navigation(startDestination = NavigationParameters.ParameterSetList.route, route = NavigationGraph.Parameters.route) {
        // region Parameter
        composable(
            route = with (NavigationParameters.ParameterView) {route + args},
            arguments = listOf(
                navArgument("parameterId") {type = NavType.IntType},
            ),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = false)
            ParameterScreen(
                viewModel = hiltViewModel<ParameterViewModel>(),
                navController = navController,
                appBarsObject = appBarsObject
            )
        }
        composable(
            route = NavigationParameters.ParameterCreate.route,
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = true)
            ParameterFormScreen(
                viewModel = hiltViewModel<ParameterFormViewModel>(),
                appBarsObject = appBarsObject,
                navController = navController,
            )
        }
        composable(
            route = with (NavigationParameters.ParameterEdit) {route + args},
            arguments = listOf(
                navArgument("parameterId") {type = NavType.IntType},
            ),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = false)
            ParameterFormScreen(
                viewModel = hiltViewModel<ParameterFormViewModel>(),
                navController = navController,
                appBarsObject = appBarsObject
            )
        }
        composable(
            route = NavigationParameters.ParameterList.route,
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
            ParameterListScreen(
                viewModel = hiltViewModel<ParameterListViewModel>(),
                navController = navController,
                appBarsObject = appBarsObject
            )
        }
        // endregion Parameter
        // region Parameter Set
        composable(
            route = with (NavigationParameters.ParameterSetView) {route + args},
            arguments = listOf(
                navArgument("parameterSetId") {type = NavType.IntType},
            ),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = false)
            ParameterSetScreen(
                viewModel = hiltViewModel<ParameterSetViewModel>(),
                navController = navController,
                appBarsObject = appBarsObject
            )
        }
        composable(
            route = NavigationParameters.ParameterSetList.route,
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
            ParameterSetListScreen(
                viewModel = hiltViewModel<ParameterSetListViewModel>(),
                navController = navController,
                appBarsObject = appBarsObject
            )
        }
        composable(
            route = with (NavigationParameters.ParameterSetEdit) {route + args},
            arguments = listOf(
                navArgument("parameterSetId") {type = NavType.IntType},
            ),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = false)
            ParameterSetFormScreen(
                viewModel = hiltViewModel<ParameterSetFormViewModel>(),
                navController = navController,
                appBarsObject = appBarsObject
            )
        }
        composable(
            route = NavigationParameters.ParameterSetCreate.route,
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = false)
            ParameterSetFormScreen(
                viewModel = hiltViewModel<ParameterSetFormViewModel>(),
                navController = navController,
                appBarsObject = appBarsObject
            )
        }
        // endregion Parameter Set
    }
}