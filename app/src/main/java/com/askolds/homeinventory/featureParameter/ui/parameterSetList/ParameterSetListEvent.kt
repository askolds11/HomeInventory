package com.askolds.homeinventory.featureParameter.ui.parameterSetList

sealed class ParameterSetListEvent {
    data class SelectItem(val id: Int, val selected: Boolean, val index: Int?): ParameterSetListEvent()
    data class QueryChanged(val query: String): ParameterSetListEvent()
    data object UnselectAll: ParameterSetListEvent()
    data object DeleteSelected: ParameterSetListEvent()
}