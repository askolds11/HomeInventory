package com.askolds.homeinventory.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.askolds.homeinventory.core.ui.fab.CreateFAB
import com.askolds.homeinventory.core.ui.fab.DeleteFAB
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.composables.SearchBar
import com.askolds.homeinventory.core.ui.navigation.composables.TopAppBar

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

/**
 * Returns whether user is currently navigating (to a different screen)
 */
@Composable
fun rememberCanNavigate(): State<Boolean> {
    // TODO: when stable - https://stackoverflow.com/questions/66546962/jetpack-compose-how-do-i-refresh-a-screen-when-app-returns-to-foreground
    val lifecycleOwner = LocalLifecycleOwner.current
    val canNavigate = remember(lifecycleOwner) {
        derivedStateOf {
            lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }
    }
    return canNavigate
}

/**
 * App bars for screens in which you can search and select items
 */
// BoxScope IS required - this is intended for use in a Box!
@Composable
fun BoxScope.SearchSelectAppBars(
    appBarsObject: AppBarsObject,
    selectedCount: Int,
    isAnySelected: Boolean,
    query: String,
    unselectAll: () -> Unit,
    searchQueryChanged: (newText: String) -> Unit,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String? = null,
) {
    if (isAnySelected) {
        TopAppBar(
            title = { Text("Selected items: $selectedCount") },
            navigationIcon = {
                IconButton(
                    onClick = unselectAll,
                    Modifier
                        .padding(horizontal = 12.dp)
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        "Unselect items",
                    )
                }
            },
            appBarsObject = appBarsObject
        )
    } else {
        SearchBar(
            query = query,
            onQueryChange = searchQueryChanged,
            clearQuery = { searchQueryChanged("") },
            appBarsObject = appBarsObject,
            trailingIcon = trailingIcon,
            placeholderText
        )
    }
}

/**
 * Create/Delete FAB for screens in which you can select items
 * @param appBarsObject App bars object
 * @param isAnySelected Whether any item is selected or not (results in create or delete fab)
 * @param deleteOnClick Delete FAB on click event
 * @param createOnClick Create FAB on click event
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.SelectCreateDeleteFAB(
    appBarsObject: AppBarsObject,
    isAnySelected: Boolean,
    deleteFABContentDescription: String,
    createFABContentDescription: String,
    deleteOnClick: () -> Unit,
    createOnClick: () -> Unit
) {
    val canNavigate = rememberCanNavigate()
    if (isAnySelected) {
        DeleteFAB(
            appBarsObject.appBarsState,
            contentDescription = deleteFABContentDescription,
            modifier = Modifier
                .padding(end = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                if (canNavigate.value)
                    deleteOnClick()
            }
        )
    } else {
        CreateFAB(
            appBarsObject.appBarsState,
            contentDescription = createFABContentDescription,
            modifier = Modifier
                .padding(end = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomEnd),
        ) {
            if (canNavigate.value)
                createOnClick()
        }
    }
}

/**
 * Get modifier for a selectable list item,
 * prevents click ripple effect when items are selected
 */
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.getSelectableClickModifier(
    isAnySelected: Boolean,
    onLongClick: () -> Unit,
    onClickWhenSelected: () -> Unit,
    onClickWhenNotSelected: () -> Unit
): Modifier = composed {
    val canNavigate = rememberCanNavigate()
    return@composed Modifier.combinedClickable(
        interactionSource = remember { MutableInteractionSource() },
        // Don't show click when selecting
        indication = if (!isAnySelected) LocalIndication.current else null,
        onLongClick = onLongClick,
        // If any item is selected, select/unselect the sequent ones, otherwise navigate
        onClick = {
            if (isAnySelected) {
                onClickWhenSelected()
            } else if (canNavigate.value) {
                onClickWhenNotSelected()
            }
        },
    )
}

/**
 * Get modifier for selected color for selectable list item
 */
fun Modifier.getSelectableColorModifier(
    isSelected: Boolean
): Modifier = composed {
    val selectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    return@composed if (isSelected)
        Modifier.background(selectedColor)
    else
        Modifier
}

/**
 * Get modifier for list item image box
 */
fun Modifier.getListItemImageModifier(): Modifier = composed {
    return@composed Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .border(
            BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            RoundedCornerShape(8.dp)
        )
}

/**
 * Make color disabled
 */
fun Color.getDisabledColor(): Color {
    return this.copy(alpha = 0.38f)
}