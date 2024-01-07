package com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.askolds.homeinventory.core.ui.SaveStatus
import com.askolds.homeinventory.core.ui.fab.NoRippleTheme
import com.askolds.homeinventory.core.ui.getDisabledColor
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme
import com.askolds.homeinventory.core.ui.theme.customColors
import com.askolds.homeinventory.featureImageNavigation.ui.detectCustomTransformGestures
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun BoxScope.ImageNavOverlayEdit(
    state: ImageNavOverlayState,
    event: (ImageNavOverlayEvent) -> Unit,
    flow: SharedFlow<Unit>,
    imageUri: String,
    appBarsObject: AppBarsObject,
    closeEditOverlay: () -> Unit,
    openThingsOverlay: () -> Unit
) {
//    flow.collectAsStateWithLifecycle(initialValue = Unit)

    val selectedItem = remember(state.selectedIndex.value, state.imageThingNavigationList) {
        val selectedIndex = state.selectedIndex.value
        if (selectedIndex != null) {
            state.imageThingNavigationList[selectedIndex]
        } else {
            null
        }
    }

    DisposableEffect(Unit) {
        event(ImageNavOverlayEvent.ClearState(true))
        onDispose {
            event(ImageNavOverlayEvent.ClearState(true))
        }
    }

    appBarsObject.appBarsState.HideBottomBar(true)

    BackHandler(
        enabled = true
    ) {
        closeEditOverlay()
    }

    /*var*/val offset by remember { mutableStateOf(Offset.Zero) }
    /*var*/val zoom by remember { mutableFloatStateOf(1f) }

    var myOffset by remember { mutableStateOf(Offset.Zero) }
    var mySize by remember { mutableStateOf(Size.Zero)}

    val screenImageX = remember { mutableFloatStateOf(0f) }
    val screenImageY = remember { mutableFloatStateOf(0f) }
//    val scaleFactorX = remember { mutableStateOf(0f) }
//    val scaleFactorY = remember { mutableStateOf(0f) }
    val emptySpaceX = remember { mutableFloatStateOf(0f) }
    val emptySpaceY = remember { mutableFloatStateOf(0f) }

    val zModifier = Modifier.zIndex(2f)
    Box(
        Modifier
            .zIndex(1f)
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .pointerInput(Unit) {
                val floatSize = size.toSize()
                screenImageX.floatValue = floatSize.width
                screenImageY.floatValue = floatSize.height
//                Log.d("Test", "draw ${screenImageX.value} ${screenImageY.value}")
                detectCustomTransformGestures(
                    onGesture = { centroid, pan, gestureZoom ->
//                        val oldScale = zoom
//                        val newScale = zoom * gestureZoom

                        // For natural zooming and rotating, the centroid of the gesture should
                        // be the fixed point where zooming and rotating occurs.
                        // We compute where the centroid was (in the pre-transformed coordinate
                        // space), and then compute where it will be after this delta.
                        // We then compute what the new offset should be to keep the centroid
                        // visually stationary for rotating and zooming, and also apply the pan.
//                        val actualCentroid = centroid.copy(
//                            x = centroid.x - emptySpaceX.value,
//                            y = centroid.y - emptySpaceY.value
//                        )
                        //offset = offset + actualCentroid / oldScale - (actualCentroid / newScale + pan / oldScale)
                        //zoom = newScale
                    },
                    onDrag = {
                            change, dragAmount ->
                        mySize = Size(mySize.width + dragAmount.x, mySize.height + dragAmount.y)
                    },
                    onDragStart = { startOffset ->
                        myOffset = startOffset.copy(
                            x = startOffset.x - emptySpaceX.floatValue,
                            y = startOffset.y - emptySpaceY.floatValue
                        )
                        mySize = Size.Zero
                    },
                    onDragEnd = {
                        event(ImageNavOverlayEvent.AddItem(myOffset, mySize))
                        myOffset = Offset.Zero
                        mySize = Size.Zero
                    },
                    onClick = { offset ->
                        event(ImageNavOverlayEvent.SelectItem(
                            offset - Offset(emptySpaceX.floatValue, emptySpaceY.floatValue)
                        ))
                    }
                )
            }
    )
    AsyncImage(
        model = imageUri,
        contentScale = ContentScale.Fit,
        contentDescription = null,
        modifier = zModifier
            .fillMaxWidth()
            .graphicsLayer {
                translationX = -offset.x * zoom
                translationY = -offset.y * zoom
                scaleX = zoom
                scaleY = zoom
                transformOrigin = TransformOrigin(0f, 0f)
            }
            .align(Alignment.Center)
            .drawWithContent {
                emptySpaceX.floatValue = (screenImageX.floatValue - size.width) / 2
                emptySpaceY.floatValue = (screenImageY.floatValue - size.height) / 2
//                scaleFactorX.value = screenImageX.value / size.width
//                scaleFactorY.value = screenImageY.value / size.height
                drawContent()
                drawRect(
                    color = Color.Red,
                    topLeft = myOffset.copy(
                        x = myOffset.x,
                        y = myOffset.y
                    ),
                    size = mySize,
                    style = Stroke(8f)
                )
                state.imageThingNavigationList.forEach {
                    drawRect(
                        color = if (it.selected) Color.Yellow else Color.Red,
                        topLeft = Offset(it.offsetX, it.offsetY),
                        size = Size(it.sizeX, it.sizeY),
                        style = Stroke(8f)
                    )
                }
            }

    )
    Column(modifier = zModifier.align(Alignment.BottomCenter)) {
        if (state.selectedIndex.value != null) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = state.thingName.value.ifBlank { null } ?: "Select thing",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.fillMaxWidth().clickable { openThingsOverlay() })
            }
        }
        BottomAppBar(
            actions = {
                IconButton(onClick = { event(ImageNavOverlayEvent.RemoveItem) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Localized description")
                }
                Text("zIndex:")
                IconButton(
                    onClick = {
                        event(ImageNavOverlayEvent.DecreaseZIndex)
                    },
                    enabled = selectedItem != null
                            && selectedItem.zIndex > 0
                            && state.imageThingNavigationList.size > 1
                ) {
                    Icon(Icons.Filled.Remove, contentDescription = "Localized description")
                }
                Text(selectedItem?.zIndex?.toString() ?: "-")
                IconButton(
                    onClick = {
                        event(ImageNavOverlayEvent.IncreaseZIndex)
                    },
                    enabled = selectedItem != null
                            && selectedItem.zIndex < state.imageThingNavigationList.lastIndex
                            && state.imageThingNavigationList.size > 1
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Localized description")
                }
            },
            floatingActionButton = {
                val isEnabled by remember(state.saveStatus) {
                    derivedStateOf {
                        state.saveStatus.value is SaveStatus.None ||
                                state.saveStatus.value is SaveStatus.Failed
                    }
                }


                fun disabledColor(color: Color): Color {
                    return if (isEnabled)
                        color
                    else
                        color.getDisabledColor()
                }

                CompositionLocalProvider(
                    // bugged on API 33
                    LocalRippleTheme provides if (isEnabled) LocalRippleTheme.current else NoRippleTheme
                ) {
                    FloatingActionButton(
                        onClick = {
                            if (isEnabled)
                                event(ImageNavOverlayEvent.Submit)
                        },
                        containerColor = disabledColor(MaterialTheme.customColors.successContainer),
                        contentColor = disabledColor(MaterialTheme.customColors.onSuccessContainer),
                    ) {
                        when (state.saveStatus.value) {
                            SaveStatus.None, SaveStatus.Failed -> {
                                Icon(
                                    Icons.Filled.Done,
                                    "Save thing"
                                )
                            }

                            SaveStatus.Saving -> {
                                CircularProgressIndicator(
                                    color = disabledColor(MaterialTheme.customColors.onSuccessContainer),
                                )
                            }

                            SaveStatus.Saved -> {
                                LaunchedEffect(Unit) {
                                    event(ImageNavOverlayEvent.ClearState(true))
                                    closeEditOverlay()
                                }
                            }
                        }
                    }
                }
            },

            )
    }

}

//@Preview
//@Composable
//fun ImageNavOverlayEditPreview() {
//    HomeInventoryTheme {
//        val state = ImageNavOverlayState()
//        Box(
//            Modifier
//                .fillMaxSize()
//                .background(color = MaterialTheme.colorScheme.background)
//        ) {
//            ImageNavOverlay(
//                state = state,
//                event = { },
//                imageUri = "",
//                appBarsObject = getPreviewAppBarsObject(),
//                closeOverlay = {}
//            )
//        }
//    }
//}