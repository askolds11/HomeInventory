package com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.core.ui.SaveStatus
import com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.model.ImageThingNavigation
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem

@Stable
class ImageNavOverlayState(
    val imageThingNavigationList: SnapshotStateList<ImageThingNavigation> = mutableStateListOf(),
    val selectedIndex: MutableState<Int?> = mutableStateOf(null),
    val saveStatus: MutableState<SaveStatus> = mutableStateOf(SaveStatus.None),
    val homeId: MutableIntState= mutableIntStateOf(0),
    val thingList: SnapshotStateList<ThingListItem> = mutableStateListOf(),
    val thingName: MutableState<String> = mutableStateOf(""),
    val navigate: MutableState<Boolean> = mutableStateOf(false)
)