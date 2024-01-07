package com.askolds.homeinventory.featureHome.ui.listScreen

sealed class HomeListEvent {
    data class SelectItem(val id: Int, val selected: Boolean, val index: Int?): HomeListEvent()
    data class QueryChanged(val query: String): HomeListEvent()
    data object UnselectAll: HomeListEvent()
    data object DeleteSelected: HomeListEvent()
}