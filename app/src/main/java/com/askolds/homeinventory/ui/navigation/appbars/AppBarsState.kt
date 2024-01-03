package com.askolds.homeinventory.ui.navigation.appbars

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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

    var canBottomScroll by mutableStateOf(true)
    var canTopScroll by mutableStateOf(true)

    var bottomFABPadding by mutableStateOf(0.dp)
        private set
    var bottomFABPaddingPx by mutableFloatStateOf(0f)
        private set
    val bottomFABPaddingInt
        get() = bottomPaddingPx.roundToInt()

    @Composable
    fun SetBottomFABPadding(padding: Dp) {
        val localDensity = LocalDensity.current
        SideEffect {
            bottomFABPadding = padding
            bottomFABPaddingPx = with(localDensity) { padding.toPx() }
        }
    }

    fun ClearBottomFABPadding() {
        bottomFABPadding = 0.dp
        bottomFABPaddingPx = 0f
    }

    @Composable
    fun SetBottomPadding(padding: Dp) {
        val localDensity = LocalDensity.current
        SideEffect {
            bottomPadding = padding
            bottomPaddingPx = with(localDensity) { padding.toPx() }
            bottomBarState.heightOffsetLimit = -bottomPaddingPx
        }
    }

    @Composable
    fun SetTopPadding(padding: Dp) {
        val localDensity = LocalDensity.current
        SideEffect {
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

    @Composable
    fun getContentPadding(includeFAB: Boolean = false): PaddingValues {
        return PaddingValues(
            top = this.topPadding,
            bottom = this.bottomPadding + (if (includeFAB) this.bottomFABPadding else 0.dp),
        )
    }

    // TODO: Functions for showing, hiding, setting paddings
}