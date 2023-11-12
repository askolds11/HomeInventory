package com.askolds.homeinventory.ui.navigation.appbars

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.abs
import kotlin.math.roundToInt

// BottomBarState is only in alpha, and combining it
// with other nestedScrolls results in some animations being sequential
// e.g. first collapses the bottom bar, then the top bar, not at the same time
// this file contains modified code from the 1.2.0-alpha10, with top bar and bottom bar
// using coroutine async to do things at the same time (onPostFling)
// https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/AppBar.kt

@Composable
fun CustomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    scrollBehavior: AppBarsScrollBehavior? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val appBarsViewModel: AppBarsViewModel = hiltViewModel()
    val navBar = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    appBarsViewModel.appBarsState.SetBottomPadding(padding = 80.dp + navBar)
    NavigationBar(
        modifier
            .layout { measurable, constraints ->
            // Sets the app bar's height offset to collapse the entire bar's height when content
            // is scrolled.
            //scrollBehavior?.bottomBarState?.heightOffsetLimit = -(appBarsViewModel.appBarsState.bottomPaddingPx)
                //ContainerHeight.toPx()

            val placeable = measurable.measure(constraints)
            val height = placeable.height + (scrollBehavior?.bottomBarState?.heightOffset ?: 0f)
            layout(placeable.width, height.roundToInt()) {
                placeable.place(0, 0)
            }
        },
        containerColor,
        contentColor,
        tonalElevation,
        windowInsets,
        content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = SearchBarDefaults.inputFieldShape,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    tonalElevation: Dp = SearchBarDefaults.Elevation,
    windowInsets: WindowInsets = SearchBarDefaults.windowInsets,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    scrollBehavior: AppBarsScrollBehavior? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val appBarsViewModel: AppBarsViewModel = hiltViewModel()
    val height = scrollBehavior?.topBarState?.heightOffset ?: 0f
    val heightDp = with(LocalDensity.current) { height.toDp() }
    val statusBar = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    appBarsViewModel.appBarsState.SetTopPadding(padding = 64.dp + statusBar)

    SearchBar(
        query,
        onQueryChange,
        onSearch,
        active,
        onActiveChange,
        modifier
            .offset(x = 0.dp, y = heightDp),
        enabled,
        placeholder,
        leadingIcon,
        trailingIcon,
        shape,
        colors,
        tonalElevation,
        windowInsets,
        interactionSource,
        content
    )
}

/**
 * A BottomAppBarScrollBehavior defines how a bottom app bar should behave when the content under
 * it is scrolled.
 *
 * @see [AppBarsDefaults.exitAlwaysScrollBehavior]
 */

@Stable
interface AppBarsScrollBehavior {

    /**
     * A [AppBarState] that is attached to this behavior and is read and updated when
     * scrolling happens.
     */
    val bottomBarState: AppBarState

    val topBarState: AppBarState

    /**
     * Indicates whether the bottom app bar is pinned.
     *
     * A pinned app bar will stay fixed in place when content is scrolled and will not react to any
     * drag gestures.
     */
    val isBottomPinned: Boolean
    val isTopPinned: Boolean

    /**
     * An optional [AnimationSpec] that defines how the bottom app bar snaps to either fully
     * collapsed or fully extended state when a fling or a drag scrolled it into an intermediate
     * position.
     */
    val bottomSnapAnimationSpec: AnimationSpec<Float>?
    val topSnapAnimationSpec: AnimationSpec<Float>?

    /**
     * An optional [DecayAnimationSpec] that defined how to fling the bottom app bar when the user
     * flings the app bar itself, or the content below it.
     */
    val bottomFlingAnimationSpec: DecayAnimationSpec<Float>?
    val topFlingAnimationSpec: DecayAnimationSpec<Float>?

    /**
     * A [NestedScrollConnection] that should be attached to a [Modifier.nestedScroll] in order to
     * keep track of the scroll events.
     */
    val nestedScrollConnection: NestedScrollConnection
}

/** Contains default values used for the bottom app bar implementations. */
object AppBarsDefaults {

    /**
     * Returns a [AppBarsScrollBehavior]. A bottom app bar that is set up with this
     * [AppBarsScrollBehavior] will immediately collapse when the content is pulled up, and
     * will immediately appear when the content is pulled down.
     *
     * @param bottomBarState the state object to be used to control or observe the bottom app bar's scroll
     * state. See [rememberAppBarState] for a state that is remembered across compositions.
     * @param canBottomScroll a callback used to determine whether scroll events are to be
     * handled by this [ExitAlwaysScrollBehavior]
     * @param snapAnimationSpec an optional [AnimationSpec] that defines how the bottom app bar
     * snaps to either fully collapsed or fully extended state when a fling or a drag scrolled it
     * into an intermediate position
     * @param flingAnimationSpec an optional [DecayAnimationSpec] that defined how to fling the
     * bottom app bar when the user flings the app bar itself, or the content below it
     */
    @Composable
    fun exitAlwaysScrollBehavior(
        bottomBarState: AppBarState = rememberAppBarState(),
        topBarState: AppBarState = rememberAppBarState(),
        canBottomScroll: () -> Boolean = { true },
        canTopScroll: () -> Boolean = { true },
        snapAnimationSpec: AnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
        flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay()
    ): AppBarsScrollBehavior =
        ExitAlwaysScrollBehavior(
            bottomBarState = bottomBarState,
            topBarState = topBarState,
            bottomSnapAnimationSpec = snapAnimationSpec,
            topSnapAnimationSpec = snapAnimationSpec,
            bottomFlingAnimationSpec = flingAnimationSpec,
            topFlingAnimationSpec = flingAnimationSpec,
            canBottomScroll = canBottomScroll,
            canTopScroll = canTopScroll,
        )
}

/**
 * Creates a [AppBarState] that is remembered across compositions.
 *
 * @param initialHeightOffsetLimit the initial value for [AppBarState.heightOffsetLimit],
 * which represents the pixel limit that a bottom app bar is allowed to collapse when the scrollable
 * content is scrolled
 * @param initialHeightOffset the initial value for [AppBarState.heightOffset]. The initial
 * offset height offset should be between zero and [initialHeightOffsetLimit].
 * @param initialContentOffset the initial value for [AppBarState.contentOffset]
 */
@Composable
fun rememberAppBarState(
    initialHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeightOffset: Float = 0f,
    initialContentOffset: Float = 0f
): AppBarState {
    return rememberSaveable(saver = AppBarState.Saver) {
        AppBarState(
            initialHeightOffsetLimit,
            initialHeightOffset,
            initialContentOffset
        )
    }
}

/**
 * A state object that can be hoisted to control and observe the bottom app bar state. The state is
 * read and updated by a [AppBarsScrollBehavior] implementation.
 *
 * In most cases, this state will be created via [rememberAppBarState].
 */
interface AppBarState {

    /**
     * The bottom app bar's height offset limit in pixels, which represents the limit that a bottom
     * app bar is allowed to collapse to.
     *
     * Use this limit to coerce the [heightOffset] value when it's updated.
     */
    var heightOffsetLimit: Float

    /**
     * The bottom app bar's current height offset in pixels. This height offset is applied to the
     * fixed height of the app bar to control the displayed height when content is being scrolled.
     *
     * Updates to the [heightOffset] value are coerced between zero and [heightOffsetLimit].
     */
    var heightOffset: Float

    /**
     * The total offset of the content scrolled under the bottom app bar.
     *
     * This value is updated by a [AppBarsScrollBehavior] whenever a nested scroll connection
     * consumes scroll events. A common implementation would update the value to be the sum of all
     * [NestedScrollConnection.onPostScroll] `consumed.y` values.
     */
    var contentOffset: Float

    /**
     * A value that represents the collapsed height percentage of the app bar.
     *
     * A `0.0` represents a fully expanded bar, and `1.0` represents a fully collapsed bar (computed
     * as [heightOffset] / [heightOffsetLimit]).
     */
    val collapsedFraction: Float

    companion object {
        /**
         * The default [Saver] implementation for [AppBarState].
         */
        val Saver: Saver<AppBarState, *> = listSaver(
            save = { listOf(it.heightOffsetLimit, it.heightOffset, it.contentOffset) },
            restore = {
                AppBarState(
                    initialHeightOffsetLimit = it[0],
                    initialHeightOffset = it[1],
                    initialContentOffset = it[2]
                )
            }
        )
    }
}

/**
 * Creates a [AppBarState].
 *
 * @param initialHeightOffsetLimit the initial value for [AppBarState.heightOffsetLimit],
 * which represents the pixel limit that a bottom app bar is allowed to collapse when the scrollable
 * content is scrolled
 * @param initialHeightOffset the initial value for [AppBarState.heightOffset]. The initial
 * offset height offset should be between zero and [initialHeightOffsetLimit].
 * @param initialContentOffset the initial value for [AppBarState.contentOffset]
 */
fun AppBarState(
    initialHeightOffsetLimit: Float,
    initialHeightOffset: Float,
    initialContentOffset: Float
): AppBarState = AppBarStateImpl(
    initialHeightOffsetLimit,
    initialHeightOffset,
    initialContentOffset
)

@Stable
private class AppBarStateImpl(
    initialHeightOffsetLimit: Float,
    initialHeightOffset: Float,
    initialContentOffset: Float
) : AppBarState {

    override var heightOffsetLimit by mutableFloatStateOf(initialHeightOffsetLimit)

    override var heightOffset: Float
        get() = _heightOffset.floatValue
        set(newOffset) {
            _heightOffset.floatValue = newOffset.coerceIn(
                minimumValue = heightOffsetLimit,
                maximumValue = 0f
            )
        }

    override var contentOffset by mutableFloatStateOf(initialContentOffset)

    override val collapsedFraction: Float
        get() = if (heightOffsetLimit != 0f) {
            heightOffset / heightOffsetLimit
        } else {
            0f
        }

    private var _heightOffset = mutableFloatStateOf(initialHeightOffset)
}

/**
 * A [AppBarsScrollBehavior] that adjusts its properties to affect the colors and height of a
 * bottom app bar.
 *
 * A bottom app bar that is set up with this [AppBarsScrollBehavior] will immediately collapse
 * when the nested content is pulled up, and will immediately appear when the content is pulled
 * down.
 *
 * @param bottomBarState a [AppBarState]
 * @param bottomSnapAnimationSpec an optional [AnimationSpec] that defines how the bottom app bar snaps to
 * either fully collapsed or fully extended state when a fling or a drag scrolled it into an
 * intermediate position
 * @param bottomFlingAnimationSpec an optional [DecayAnimationSpec] that defined how to fling the bottom
 * app bar when the user flings the app bar itself, or the content below it
 * @param canBottomScroll a callback used to determine whether scroll events are to be
 * handled by this [ExitAlwaysScrollBehavior]
 */
private class ExitAlwaysScrollBehavior(
    override val bottomBarState: AppBarState,
    override val topBarState: AppBarState,
    override val bottomSnapAnimationSpec: AnimationSpec<Float>?,
    override val topSnapAnimationSpec: AnimationSpec<Float>?,
    override val bottomFlingAnimationSpec: DecayAnimationSpec<Float>?,
    override val topFlingAnimationSpec: DecayAnimationSpec<Float>?,
    val canBottomScroll: () -> Boolean = { true },
    val canTopScroll: () -> Boolean = { true },
) : AppBarsScrollBehavior {
    override val isBottomPinned: Boolean = false
    override val isTopPinned: Boolean = false
    override var nestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (canBottomScroll()) {
                    bottomBarState.contentOffset += consumed.y
                    if (bottomBarState.heightOffset == 0f || bottomBarState.heightOffset == bottomBarState.heightOffsetLimit) {
                        if (consumed.y == 0f && available.y > 0f) {
                            // Reset the total content offset to zero when scrolling all the way down.
                            // This will eliminate some float precision inaccuracies.
                            bottomBarState.contentOffset = 0f
                        }
                    }
                    bottomBarState.heightOffset = bottomBarState.heightOffset + consumed.y
                }

                if (canTopScroll()) {
                    topBarState.contentOffset += consumed.y
                    if (topBarState.heightOffset == 0f || topBarState.heightOffset == topBarState.heightOffsetLimit) {
                        if (consumed.y == 0f && available.y > 0f) {
                            // Reset the total content offset to zero when scrolling all the way down.
                            // This will eliminate some float precision inaccuracies.
                            topBarState.contentOffset = 0f
                        }
                    }
                    topBarState.heightOffset = topBarState.heightOffset + consumed.y
                }

                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val superConsumed = super.onPostFling(consumed, available)

                val settledValue = coroutineScope {
                    val bottom = async {
                        return@async settleAppBar(
                            bottomBarState,
                            available.y,
                            bottomFlingAnimationSpec,
                            bottomSnapAnimationSpec
                        )
                    }
                    val top = async {
                        return@async if (
                            topBarState.contentOffset < topBarState.heightOffsetLimit //||
                            //(topBarState.contentOffset <= topBarState.heightOffset && topBarState.wasLastHidden)
                            ) {
                             settleAppBar(
                                topBarState,
                                available.y,
                                topFlingAnimationSpec,
                                topSnapAnimationSpec
                            )
                        } else {
                            Velocity.Zero
                        }
                    }
                    return@coroutineScope bottom.await() + top.await()
                }

                return superConsumed + settledValue
            }
        }
}

/**
 * Settles the app bar by flinging, in case the given velocity is greater than zero, and snapping
 * after the fling settles.
 */
private suspend fun settleAppBar(
    state: AppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?
): Velocity {
    // Check if the app bar is completely collapsed/expanded. If so, no need to settle the app bar,
    // and just return Zero Velocity.
    // Note that we don't check for 0f due to float precision with the collapsedFraction
    // calculation.
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    // In case there is an initial velocity that was left after a previous user fling, animate to
    // continue the motion to expand or collapse the app bar.
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    // Snap if animation specs were provided.
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 &&
            state.heightOffset > state.heightOffsetLimit
        ) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec
            ) { state.heightOffset = value }
        }
    }

    return Velocity(0f, remainingVelocity)
}