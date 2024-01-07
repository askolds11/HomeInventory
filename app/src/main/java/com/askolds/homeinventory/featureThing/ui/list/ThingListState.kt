package com.askolds.homeinventory.featureThing.ui.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem

@Stable
class ThingListState (
    val thingList: SnapshotStateList<ThingListItem> = mutableStateListOf(),
    val homeId: MutableState<Int> = mutableIntStateOf(0),
    val thingId: MutableState<Int?> = mutableStateOf(null),
    val isContainer: MutableState<Boolean> = mutableStateOf(false),
    val query: MutableState<String> = mutableStateOf(""),
    val selectedCount: MutableState<Int> = mutableIntStateOf(0)
)