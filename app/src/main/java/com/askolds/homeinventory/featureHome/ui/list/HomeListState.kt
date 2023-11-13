package com.askolds.homeinventory.featureHome.ui.list

import com.askolds.homeinventory.featureHome.domain.model.HomeListItem

data class HomeListState (
    val homeList: List<HomeListItem> = emptyList(),
)