package com.askolds.homeinventory.featureThing.ui.list

sealed class ThingListEvent {
    data class SelectItem(val id: Int, val selected: Boolean, val index: Int?): ThingListEvent()
    data class QueryChanged(val query: String): ThingListEvent()
    data object UnselectAll: ThingListEvent()
    data object DeleteSelected: ThingListEvent()
}