package com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

sealed class ImageNavOverlayEvent {
    data class AddItem(val offset: Offset, val size: Size) : ImageNavOverlayEvent()
    data object RemoveItem: ImageNavOverlayEvent()
    data object DecreaseZIndex : ImageNavOverlayEvent()
    data object IncreaseZIndex : ImageNavOverlayEvent()
    data class SelectItem(val position: Offset) : ImageNavOverlayEvent()
    data object Submit : ImageNavOverlayEvent()
    data class ClearState(val editMode: Boolean = false) : ImageNavOverlayEvent()
    data class SelectThing(val thingId: Int) : ImageNavOverlayEvent()
}