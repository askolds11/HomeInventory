package com.askolds.homeinventory.featureParameter.ui.parameterSetListScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSetListItem

data class ParameterSetListState (
    // Seems like copying the item inside a list is slow (noticeable small delay when selecting items using short click)
    // have to use mutable state list
    val parameterSetList: SnapshotStateList<ParameterSetListItem> = mutableStateListOf(),
    val query: String = "",
    val selectedCount: Int = 0
)