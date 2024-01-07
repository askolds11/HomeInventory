package com.askolds.homeinventory.featureParameter.ui.parameterFormScreen

sealed class ParameterFormEvent {
    data class NameChanged(val name: String): ParameterFormEvent()
    data object Submit: ParameterFormEvent()
}