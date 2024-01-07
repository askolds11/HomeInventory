package com.askolds.homeinventory.featureThing.ui.formScreen.parameterSets

sealed class ThingParameterSetsEvent {
    data class SelectItem(val id: Int, val selected: Boolean, val index: Int?): ThingParameterSetsEvent()
    data class QueryChanged(val query: String): ThingParameterSetsEvent()
    data object UnselectAll: ThingParameterSetsEvent()
}