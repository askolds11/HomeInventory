package com.askolds.homeinventory.featureThing.ui.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem

data class ThingListState (
    val thingList: SnapshotStateList<ThingListItem> = mutableStateListOf(),
    val homeId: Int = 0,
    val thingId: Int? = null,
    val isContainer: Boolean = false,
    val query: String = "",
    val selectedCount: Int = 0
)