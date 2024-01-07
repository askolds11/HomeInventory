package com.askolds.homeinventory.core.ui.navigation.appbars

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class AppBarsState @Inject constructor(
    @Named("topBarState") val topBarState: AppBarState,
    @Named("bottomBarState") val bottomBarState: AppBarState
) {
    var bottomPaddingLimit by mutableStateOf(0.dp)
        private set
    var bottomPaddingLimitPx by mutableFloatStateOf(0f)
        private set
    var topPaddingLimit by mutableStateOf(0.dp)
        private set
    var topPaddingLimitPx by mutableFloatStateOf(0f)
        private set

    var canBottomScroll by mutableStateOf(true)
    var canTopScroll by mutableStateOf(true)

    private var bottomFABPadding by mutableStateOf(0.dp)
        private set
    private var bottomFABPaddingPx by mutableFloatStateOf(0f)
        private set

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
            bottomPaddingLimit = padding
            bottomPaddingLimitPx = with(localDensity) { padding.toPx() }
            bottomBarState.heightOffsetLimit = -bottomPaddingLimitPx
        }
    }

    @Composable
    fun SetTopPadding(padding: Dp) {
        val localDensity = LocalDensity.current
        SideEffect {
            topPaddingLimit = padding
            topPaddingLimitPx = with(localDensity) { padding.toPx() }
            topBarState.heightOffsetLimit = -topPaddingLimitPx
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
    suspend fun showBottomBar(lock: Boolean) {
        withContext (Dispatchers.Main) {
            val state = bottomBarState
            AnimationState(initialValue = state.heightOffset).animateTo(
                0f,
            ) { state.heightOffset = value }
        }
        canBottomScroll = !lock
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
    fun getContentPadding(includeFAB: Boolean = false, includeKeyboard: Boolean = false): PaddingValues {
        val localDensity = LocalDensity.current

        return PaddingValues(
            top = this.topPaddingLimit,
            bottom = this.bottomPaddingLimit
                    + (if (includeFAB) this.bottomFABPadding else 0.dp)
                    + (if (includeKeyboard) WindowInsets.ime.asPaddingValues().calculateBottomPadding() else 0.dp),
        )
    }

    // TODO: Functions for showing, hiding, setting paddings
}