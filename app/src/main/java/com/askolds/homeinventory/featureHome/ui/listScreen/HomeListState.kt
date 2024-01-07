package com.askolds.homeinventory.featureHome.ui.listScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureHome.domain.model.HomeListItem

@Stable
class HomeListState(
    val homeList: SnapshotStateList<HomeListItem> = mutableStateListOf(),
    val query: MutableState<String> = mutableStateOf(""),
    val selectedCount: MutableState<Int> = mutableIntStateOf(0)
)