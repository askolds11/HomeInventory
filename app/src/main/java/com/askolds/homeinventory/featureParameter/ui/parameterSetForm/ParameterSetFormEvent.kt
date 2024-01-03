package com.askolds.homeinventory.featureParameter.ui.parameterSetForm

sealed class ParameterSetFormEvent {
    data class NameChanged(val name: String): ParameterSetFormEvent()
    data class SelectItem(val id: Int, val selected: Boolean, val index: Int?): ParameterSetFormEvent()
    data object UnselectAll: ParameterSetFormEvent()
    data object Submit: ParameterSetFormEvent()
}