package com.askolds.homeinventory.featureParameter.ui.parameterList

sealed class ParameterListEvent {
    data class SelectItem(val id: Int, val selected: Boolean, val index: Int?): ParameterListEvent()
    data class QueryChanged(val query: String): ParameterListEvent()
    data object UnselectAll: ParameterListEvent()
    data object DeleteSelected: ParameterListEvent()
}