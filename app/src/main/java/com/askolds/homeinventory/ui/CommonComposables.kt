package com.askolds.homeinventory.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Helper function for getting padding with at least safeDrawing
 * @property start Desired start padding
 * @property end Desired end padding
 * @property top Desired top padding
 * @property bottom Desired bottom padding
 */
@Composable
fun getDrawingPadding(start: Dp = 0.dp, end: Dp = 0.dp, top: Dp = 0.dp, bottom: Dp = 0.dp): PaddingValues {
    fun compareAndGet(safePadding: Dp, desiredPadding: Dp): Dp {
        return if (safePadding > desiredPadding) safePadding else desiredPadding
    }

    val safeDrawing = WindowInsets.safeDrawing.asPaddingValues()
    return PaddingValues(
        start = compareAndGet(safeDrawing.calculateStartPadding(LocalLayoutDirection.current), start),
        end =  compareAndGet(safeDrawing.calculateEndPadding(LocalLayoutDirection.current), end),
        bottom =  /*compareAndGet(BottomBar.padding, bottom),*/ bottom, //TODO: FIX
        top =  compareAndGet(safeDrawing.calculateTopPadding(), top)
    )
}