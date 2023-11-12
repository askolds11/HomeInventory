package com.askolds.homeinventory.ui.navigation.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.askolds.homeinventory.featureHome.ui.NavigationHome
import com.askolds.homeinventory.featureHome.ui.list.HomeListFAB
import com.askolds.homeinventory.featureHome.ui.list.HomeListViewModel
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsViewModel
import kotlinx.coroutines.launch

@Composable
fun FAB(navController: NavController) {
    val appBarsViewModel: AppBarsViewModel = hiltViewModel()
    val appBarsState = appBarsViewModel.appBarsState
    val bottomSafeDrawing = WindowInsets.safeDrawing.getBottom(LocalDensity.current)

    val fabOffsetFloat = remember {
        derivedStateOf {
            if (appBarsState.bottomPaddingInt + appBarsState.bottomBarState.heightOffset < bottomSafeDrawing) {
                return@derivedStateOf -bottomSafeDrawing.toFloat() - (appBarsState.bottomBarState.heightOffsetLimit - appBarsState.bottomBarState.heightOffset)
            } else {
                return@derivedStateOf 0f
            }
        }

    }
    val fabOffsetDp = with (LocalDensity.current) { fabOffsetFloat.value.toDp() }

    val offsetModifier = Modifier
        .offset(y = fabOffsetDp)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    when (currentDestination?.route) {
        NavigationHome.List.route -> {
//            val parentEntry = remember(navBackStackEntry) {
//                navController.getBackStackEntry(NavigationHome.List.route)
//            }
//            val homeListViewModel = hiltViewModel<HomeListViewModel>(parentEntry)
            CreateFAB(
                { navController.navigate(route = NavigationHome.Create.route) },
                offsetModifier
            )
        }
    }
}

@Composable
private fun CreateFAB(
    onClick: () -> Unit,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    FloatingActionButton(
        onClick = { onClick() },
        modifier) {
        Icon(
            Icons.Filled.Add,
            "Add home"
        )
    }
}