package com.askolds.homeinventory.featureImageNavigation.ui

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.dp
import com.askolds.homeinventory.core.ui.fastForEach
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sign

// https://github.com/androidx/androidx/blob/2436c009f6e19878390e44eecfbff650f177d940/compose/foundation/foundation/src/commonMain/kotlin/androidx/compose/foundation/gestures/TransformGestureDetector.kt#L47
suspend fun PointerInputScope.detectCustomTransformGestures(
    onGesture: (centroid: Offset, pan: Offset, zoom: Float) -> Unit,
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit,
    onClick: (Offset) -> Unit,
) {
    awaitEachGesture {
        val rotation = 0f
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop

        var event: PointerEvent

        var drag: PointerInputChange? = null
        var overSlop = Offset.Zero

        val down = awaitFirstDown(requireUnconsumed = false)
        do {
            event = awaitPointerEvent()
            var canceled = false

            if (event.changes.size == 1) {
                val dragAndEvent = awaitCustomPointerSlopOrCancellation(
                    event,
                    down.id,
                    down.type,
                    triggerOnMainAxisSlop = false
                ) { change, over ->
                    change.consume()
                    overSlop = over
                }
                drag = dragAndEvent.first
                if (dragAndEvent.second != null) event = dragAndEvent.second!!

                // click
                if (event.type == PointerEventType.Release) {
                    Log.d("Test", "$event")
                    onClick(event.changes[0].position)
                }
            }
            if (event.changes.size > 1) {
                canceled = event.changes.fastAny { it.isConsumed }
                if (!canceled) {
                    val zoomChange = event.calculateZoom()
                    val panChange = event.calculatePan()

                    if (!pastTouchSlop) {
                        zoom *= zoomChange
                        pan += panChange

                        val centroidSize = event.calculateCentroidSize(useCurrent = false)
                        val zoomMotion = abs(1 - zoom) * centroidSize
                        val rotationMotion = abs(rotation * PI.toFloat() * centroidSize / 180f)
                        val panMotion = pan.getDistance()

                        if (zoomMotion > touchSlop ||
                            rotationMotion > touchSlop ||
                            panMotion > touchSlop
                        ) {
                            pastTouchSlop = true
                        }
                    }

                    if (pastTouchSlop) {
                        val centroid = event.calculateCentroid(useCurrent = false)
                        if (zoomChange != 1f ||
                            panChange != Offset.Zero
                        ) {
                            onGesture(centroid, panChange, zoomChange)
                        }
                        event.changes.fastForEach {
                            if (it.positionChanged()) {
                                it.consume()
                            }
                        }
                    }
                }
            }
        } while (
            event.changes.size > 1 && !canceled && event.changes.fastAny { it.pressed }
            || event.changes.size == 1 && drag != null && !drag.isConsumed
        )
        if (event.changes.size == 1) {
            if (drag != null) {
                onDragStart.invoke(drag.position)
                onDrag(drag, overSlop)
                if (
                    !drag(drag.id) {
                        onDrag(it, it.positionChange())
                        it.consume()
                    }
                ) {
                    onDragCancel()
                } else {
                    onDragEnd()
                }
            }
        }
    }
}

@OptIn(ExperimentalContracts::class)
private inline fun <T> List<T>.fastAny(predicate: (T) -> Boolean): Boolean {
    contract { callsInPlace(predicate) }
    fastForEach { if (predicate(it)) return true }
    return false
}

/**
 * Configures the calculations to convert offset to deltas in the Main and Cross Axis.
 * [offsetFromChanges] will also change depending on implementation.
 */
private interface PointerDirectionConfig {
    fun mainAxisDelta(offset: Offset): Float
    fun crossAxisDelta(offset: Offset): Float
    fun offsetFromChanges(mainChange: Float, crossChange: Float): Offset
}

/**
 * Used for monitoring changes on X axis.
 */
private val HorizontalPointerDirectionConfig = object : PointerDirectionConfig {
    override fun mainAxisDelta(offset: Offset): Float = offset.x
    override fun crossAxisDelta(offset: Offset): Float = offset.y
    override fun offsetFromChanges(mainChange: Float, crossChange: Float): Offset =
        Offset(mainChange, crossChange)
}

/**
 * Used for monitoring changes on Y axis.
 */
private val VerticalPointerDirectionConfig = object : PointerDirectionConfig {
    override fun mainAxisDelta(offset: Offset): Float = offset.y

    override fun crossAxisDelta(offset: Offset): Float = offset.x

    override fun offsetFromChanges(mainChange: Float, crossChange: Float): Offset =
        Offset(crossChange, mainChange)
}

private fun Orientation.toPointerDirectionConfig(): PointerDirectionConfig =
    if (this == Orientation.Vertical) VerticalPointerDirectionConfig
    else HorizontalPointerDirectionConfig

private fun PointerEvent.isPointerUp(pointerId: PointerId): Boolean =
    changes.fastFirstOrNull { it.id == pointerId }?.pressed != true

private val mouseSlop = 0.125.dp
private val defaultTouchSlop = 18.dp // The default touch slop on Android devices
private val mouseToTouchSlopRatio = mouseSlop / defaultTouchSlop

private fun ViewConfiguration.pointerSlop(pointerType: PointerType): Float {
    return when (pointerType) {
        PointerType.Mouse -> touchSlop * mouseToTouchSlopRatio
        else -> touchSlop
    }
}

/**
 * Returns the first value that [predicate] returns `true` for or `null` if nothing matches.
 *
 * **Do not use for collections that come from public APIs**, since they may not support random
 * access in an efficient way, and this method may actually be a lot slower. Only use for
 * collections that are created by code we control and are known to support random access.
 */
@Suppress("BanInlineOptIn")
@OptIn(ExperimentalContracts::class)
private inline fun <T> List<T>.fastFirstOrNull(predicate: (T) -> Boolean): T? {
    contract { callsInPlace(predicate) }
    fastForEach { if (predicate(it)) return it }
    return null
}

/**
 * Returns `true` if all elements match the given [predicate].
 *
 * **Do not use for collections that come from public APIs**, since they may not support random
 * access in an efficient way, and this method may actually be a lot slower. Only use for
 * collections that are created by code we control and are known to support random access.
 */
@Suppress("BanInlineOptIn")
@OptIn(ExperimentalContracts::class)
private inline fun <T> List<T>.fastAll(predicate: (T) -> Boolean): Boolean {
    contract { callsInPlace(predicate) }
    fastForEach { if (!predicate(it)) return false }
    return true
}

private suspend inline fun AwaitPointerEventScope.awaitCustomPointerSlopOrCancellation(
    firstEvent: PointerEvent,
    pointerId: PointerId,
    pointerType: PointerType,
    pointerDirectionConfig: PointerDirectionConfig = HorizontalPointerDirectionConfig,
    triggerOnMainAxisSlop: Boolean = true,
    onPointerSlopReached: (PointerInputChange, Offset) -> Unit,
): Pair<PointerInputChange?, PointerEvent?> {
    if (currentEvent.isPointerUp(pointerId)) {
        return Pair(null, null) // The pointer has already been lifted, so the gesture is canceled
    }
    val touchSlop = viewConfiguration.pointerSlop(pointerType)
    var pointer: PointerId = pointerId
    var totalMainPositionChange = 0f
    var totalCrossPositionChange = 0f

    var event = firstEvent
    while (true) {
        if (event.changes.size > 1) return Pair(null, event)
        val dragEvent = event.changes.fastFirstOrNull { it.id == pointer } ?: return Pair(null, event)
        if (dragEvent.isConsumed) {
            return Pair(null, null)
        } else if (dragEvent.changedToUpIgnoreConsumed()) {
            val otherDown = event.changes.fastFirstOrNull { it.pressed }
            if (otherDown == null) {
                // This is the last "up"
                return Pair(null, null)
            } else {
                pointer = otherDown.id
            }
        } else {
            val currentPosition = dragEvent.position
            val previousPosition = dragEvent.previousPosition

            val mainPositionChange = pointerDirectionConfig.mainAxisDelta(currentPosition) -
                    pointerDirectionConfig.mainAxisDelta(previousPosition)

            val crossPositionChange = pointerDirectionConfig.crossAxisDelta(currentPosition) -
                    pointerDirectionConfig.crossAxisDelta(previousPosition)
            totalMainPositionChange += mainPositionChange
            totalCrossPositionChange += crossPositionChange

            val inDirection = if (triggerOnMainAxisSlop) {
                abs(totalMainPositionChange)
            } else {
                pointerDirectionConfig.offsetFromChanges(
                    totalMainPositionChange,
                    totalCrossPositionChange
                ).getDistance()
            }
            if (inDirection < touchSlop) {
                // verify that nothing else consumed the drag event
                awaitPointerEvent(PointerEventPass.Final)
                if (dragEvent.isConsumed) {
                    return Pair(null, null)
                }
            } else {
                val postSlopOffset = if (triggerOnMainAxisSlop) {
                    val finalMainPositionChange = totalMainPositionChange -
                            (sign(totalMainPositionChange) * touchSlop)
                    pointerDirectionConfig.offsetFromChanges(
                        finalMainPositionChange,
                        totalCrossPositionChange
                    )
                } else {
                    val offset = pointerDirectionConfig.offsetFromChanges(
                        totalMainPositionChange,
                        totalCrossPositionChange
                    )
                    val touchSlopOffset = offset / inDirection * touchSlop
                    offset - touchSlopOffset
                }

                onPointerSlopReached(
                    dragEvent,
                    postSlopOffset
                )
                if (dragEvent.isConsumed) {
                    return Pair(dragEvent, null)
                } else {
                    totalMainPositionChange = 0f
                    totalCrossPositionChange = 0f
                }
            }
        }
        event = awaitPointerEvent()
    }
}

