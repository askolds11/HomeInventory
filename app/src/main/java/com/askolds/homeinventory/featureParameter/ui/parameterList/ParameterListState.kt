package com.askolds.homeinventory.featureParameter.ui.parameterList

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem

data class ParameterListState (
    val parameterList: SnapshotStateList<ParameterListItem> = mutableStateListOf(),
//    val homeId: Int = 0,
//    val thingId: Int? = null,
    val query: String = "",
    val selectedCount: Int = 0
)