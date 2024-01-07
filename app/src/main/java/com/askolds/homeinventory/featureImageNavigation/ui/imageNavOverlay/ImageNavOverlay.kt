package com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme
import com.askolds.homeinventory.featureImageNavigation.ui.detectCustomTransformGestures
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.ImageNavOverlay(
    state: ImageNavOverlayState,
    event: (ImageNavOverlayEvent) -> Unit,
    flow: SharedFlow<Unit>,
    imageUri: String,
    appBarsObject: AppBarsObject,
    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
    closeOverlay: () -> Unit,
    openEditOverlay: () -> Unit,
) {
    flow.collectAsStateWithLifecycle(initialValue = Unit)

    val selectedItem = remember(state.selectedIndex.value, state.imageThingNavigationList) {
        val selectedIndex = state.selectedIndex.value
        if (selectedIndex != null) {
            state.imageThingNavigationList[selectedIndex]
        } else {
            null
        }
    }

    appBarsObject.appBarsState.HideBottomBar(true)

    BackHandler(
        enabled = true
    ) {
        closeOverlay()
    }

    LaunchedEffect(state.navigate.value) {
        Log.d("Test", state.navigate.value.toString())
        Log.d("Test", selectedItem?.thingId.toString())
        if (state.navigate.value) {
            if (selectedItem?.thingId != null)
                navigateToThing(state.homeId.intValue, selectedItem.thingId)
        }
    }

    DisposableEffect(Unit) {

        onDispose {
            event(ImageNavOverlayEvent.ClearState(true))
        }
    }

    /*var*/val offset by remember { mutableStateOf(Offset.Zero) }
    /*var*/val zoom by remember { mutableFloatStateOf(1f) }

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
                detectTapGestures {
                    event(
                        ImageNavOverlayEvent.SelectItem(
                            it - Offset(emptySpaceX.floatValue, emptySpaceY.floatValue)
                        )
                    )
                }
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
                drawContent()
                state.imageThingNavigationList.forEach {
                    drawRect(
                        color = Color.Red,
                        topLeft = Offset(it.offsetX, it.offsetY),
                        size = Size(it.sizeX, it.sizeY),
                        style = Stroke(8f)
                    )
                }
            }

    )
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(
                onClick = {
                    closeOverlay()
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    stringResource(R.string.navigate_back),
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    openEditOverlay()
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    stringResource(R.string.edit_parameter),
                )
            }
        },
        modifier = Modifier.zIndex(5f)
    )
}

@Preview
@Composable
fun ImageNavOverlayPreview() {
    HomeInventoryTheme {
        val state = ImageNavOverlayState()
        Box(
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            ImageNavOverlay(
                state = state,
                event = { },
                flow = MutableSharedFlow(),
                imageUri = "",
                appBarsObject = getPreviewAppBarsObject(),
                navigateToThing = {_, _ -> },
                closeOverlay = {},
                openEditOverlay = {}
            )
        }
    }
}