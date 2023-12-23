package com.askolds.homeinventory.featureParameter.ui.parameterSetList

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem
data class ParameterSetListState (
    val parameterSetList: SnapshotStateList<ParameterListItem> = mutableStateListOf(),
    val homeId: Int = 0,
    val thingId: Int? = null,
    val isContainer: Boolean = false,
    val query: String = "",
    val selectedCount: Int = 0
)