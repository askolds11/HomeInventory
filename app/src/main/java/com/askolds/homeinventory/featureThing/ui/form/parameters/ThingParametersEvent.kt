package com.askolds.homeinventory.featureThing.ui.form.parameters

sealed class ThingParametersEvent {
    data class SelectItem(val id: Int, val selected: Boolean, val index: Int?): ThingParametersEvent()
    data class QueryChanged(val query: String): ThingParametersEvent()
    data object UnselectAll: ThingParametersEvent()
}