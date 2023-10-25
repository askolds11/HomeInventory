package com.askolds.homeinventory.ui.navigation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

// https://stackoverflow.com/questions/77197810/how-to-detect-if-user-is-scrolling-up-down-lazycolumn-jetpack-compose

/*
    Determines the user's scroll direction.
 */

enum class ScrollDirection {
    Up, Down, None
}

@Stable
class DirectionalLazyListState(
    private val lazyListState: LazyListState,
    private val scrollThreshold: Int,
    coroutineScope: CoroutineScope
) {
    private var positionY = lazyListState.firstVisibleItemScrollOffset
    private var visibleItem = lazyListState.firstVisibleItemIndex

    private var currentTime = System.currentTimeMillis()
    var scrollDirection by mutableStateOf(ScrollDirection.None)

    init {

        coroutineScope.launch {
            while (isActive) {
                delay(120)
                if (System.currentTimeMillis() - currentTime > 120) {
                    scrollDirection = ScrollDirection.None
                }
            }
        }

        snapshotFlow {
            val scrollInt = if (lazyListState.isScrollInProgress) 20000 else 10000
            val visibleItemInt = lazyListState.firstVisibleItemIndex * 10
            scrollInt + visibleItemInt + lazyListState.firstVisibleItemScrollOffset
        }
            .onEach {
                if (lazyListState.isScrollInProgress.not()) {
                    scrollDirection = ScrollDirection.None
                } else {

                    currentTime = System.currentTimeMillis()

                    val firstVisibleItemIndex = lazyListState.firstVisibleItemIndex
                    val firstVisibleItemScrollOffset =
                        lazyListState.firstVisibleItemScrollOffset

                    // We are scrolling while first visible item hasn't changed yet
                    if (firstVisibleItemIndex == visibleItem) {
                        if (abs(firstVisibleItemScrollOffset - positionY) > scrollThreshold) { // Add this line
                            val direction = if (firstVisibleItemScrollOffset > positionY) {
                                ScrollDirection.Down
                            } else {
                                ScrollDirection.Up
                            }
                            positionY = firstVisibleItemScrollOffset

                            scrollDirection = direction
                        }
                    } else {

                        val direction = if (firstVisibleItemIndex > visibleItem) {
                            ScrollDirection.Down
                        } else {
                            ScrollDirection.Up
                        }
                        positionY = firstVisibleItemScrollOffset
                        visibleItem = firstVisibleItemIndex
                        scrollDirection = direction
                    }
                }
            }
            .launchIn(coroutineScope)
    }
}

@Composable
fun rememberDirectionalLazyListState(
    lazyListState: LazyListState,
    scrollThreshold: Int,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): DirectionalLazyListState {
    return remember {
        DirectionalLazyListState(lazyListState, scrollThreshold, coroutineScope)
    }
}