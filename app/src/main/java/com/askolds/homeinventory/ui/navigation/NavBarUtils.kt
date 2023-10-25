package com.askolds.homeinventory.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

/**
 * Class for CompositionLocal of the NavBar
 * @property barVisible Navigation bar visibility
 * @property bottomBarPadding Navigation Bar padding value
 */
class NavBarUtils {
    private val _barVisible = mutableStateOf(true)
    private val _bottomBarPadding = mutableStateOf(0.dp)

    var barVisible by _barVisible
    var bottomBarPadding by _bottomBarPadding
}
val LocalNavBar = compositionLocalOf { NavBarUtils() }

/**
 *  Composable for scrollable screen navigation bar hiding
 *  @property lazyListState LazyListState of the screen's LazyColumn
 *  @property scrollThreshold Minimum scroll amount for scrolling to be registered. Depends on item height.
 */
@Composable
fun NavBarVisibility(
    lazyListState: LazyListState,
    scrollThreshold: Int
) {
    var barVisible by remember { mutableStateOf(true) }
    val directionalLazyListState = rememberDirectionalLazyListState(lazyListState = lazyListState, scrollThreshold = scrollThreshold)

    LocalNavBar.current.barVisible = remember(lazyListState) {
        derivedStateOf {
            if (barVisible && directionalLazyListState.scrollDirection == ScrollDirection.Down) false
            else if (!barVisible && directionalLazyListState.scrollDirection == ScrollDirection.Up) true
            else barVisible
        }
    }.value
    barVisible = LocalNavBar.current.barVisible
}

/**
 * Sets the LocalNavBar.bottomBarPadding to the maximum value
 *
 * Only for use in the main Scaffold composable!
 *
 * @param paddingValues The navigation bar padding values<br>
 */
@Composable
fun NavBarBottomPadding(
    paddingValues: PaddingValues
) {
    val navBarPadding = remember { mutableStateOf(0.dp) }
    LocalNavBar.current.bottomBarPadding = navBarPadding.value
    LaunchedEffect(paddingValues) {
        if (paddingValues.calculateBottomPadding() > navBarPadding.value) {
            navBarPadding.value = paddingValues.calculateBottomPadding()
        }
    }
}