package com.askolds.homeinventory.ui.navigation.appbars

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt

class AppBarsState @Inject constructor(
    @Named("topBarState") val topBarState: AppBarState,
    @Named("bottomBarState") val bottomBarState: AppBarState
) {
    var bottomPadding by mutableStateOf(0.dp)
        private set
    var bottomPaddingPx by mutableFloatStateOf(0f)
        private set
    val bottomPaddingInt
        get() = bottomPaddingPx.roundToInt()
    var topPadding by mutableStateOf(0.dp)
        private set
    var topPaddingPx by mutableFloatStateOf(0f)
        private set
    val topPaddingInt
        get() = topPaddingPx.roundToInt()

    var resetBottomPadding by mutableStateOf(false)
    var resetTopPadding by mutableStateOf(false)

    var canBottomScroll by mutableStateOf(true)
    var canTopScroll by mutableStateOf(true)

    @Composable
    fun SetBottomPadding(padding: Dp) {
        val localDensity = LocalDensity.current
        LaunchedEffect(Unit) {
            bottomPadding = padding
            bottomPaddingPx = with(localDensity) { padding.toPx() }
            bottomBarState.heightOffsetLimit = -bottomPaddingPx
        }
    }

    @Composable
    fun SetTopPadding(padding: Dp) {
        val localDensity = LocalDensity.current
        LaunchedEffect(Unit) {
            topPadding = padding
            topPaddingPx = with(localDensity) { padding.toPx() }
            topBarState.heightOffsetLimit = -topPaddingPx
        }
    }

    @Composable
    fun ShowAppBars(lockTop: Boolean, lockBottom: Boolean) {
        ShowBottomBar(lockBottom)
        ShowTopBar(lockTop)
    }

    @Composable
    fun ShowBottomBar(lock: Boolean) {
        LaunchedEffect(Unit) {
            coroutineScope {
                launch {
                    val state = bottomBarState
                    AnimationState(initialValue = state.heightOffset).animateTo(
                        0f,
                    ) { state.heightOffset = value }
                }
            }
            canBottomScroll = !lock
        }
    }

    @Composable
    fun ShowTopBar(lock: Boolean) {
        LaunchedEffect(Unit) {
            coroutineScope {
                launch {
                    val state = topBarState
                    AnimationState(initialValue = state.heightOffset).animateTo(
                        0f,
                    ) { state.heightOffset = value }
                }
            }
            canTopScroll = !lock
        }
    }

    @Composable
    fun HideAppBars(lockTop: Boolean, lockBottom: Boolean) {
        HideBottomBar(lockBottom)
        HideTopBar(lockTop)
    }

    @Composable
    fun HideBottomBar(lock: Boolean) {
        LaunchedEffect(Unit) {
            coroutineScope {
                launch {
                    val state = bottomBarState
                    AnimationState(initialValue = state.heightOffset).animateTo(
                        state.heightOffsetLimit,
                    ) { state.heightOffset = value }
                }
            }
            canBottomScroll = !lock
        }
    }

    @Composable
    fun HideTopBar(lock: Boolean) {
        LaunchedEffect(Unit) {
            coroutineScope {
                launch {
                    val state = topBarState
                    AnimationState(initialValue = state.heightOffset).animateTo(
                        state.heightOffsetLimit,
                    ) { state.heightOffset = value }
                }
            }
            canTopScroll = !lock
        }
    }

    // TODO: Functions for showing, hiding, setting paddings
}