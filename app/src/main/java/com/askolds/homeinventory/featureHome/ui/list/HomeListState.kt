package com.askolds.homeinventory.featureHome.ui.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureHome.domain.model.HomeListItem

data class HomeListState(
    val homeList: SnapshotStateList<HomeListItem> = mutableStateListOf(),
    val query: String = "",
    val selectedCount: Int = 0
)