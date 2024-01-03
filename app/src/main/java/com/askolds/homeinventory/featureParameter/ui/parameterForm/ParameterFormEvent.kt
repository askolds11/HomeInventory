package com.askolds.homeinventory.featureParameter.ui.parameterForm

sealed class ParameterFormEvent {
    data class NameChanged(val name: String): ParameterFormEvent()
    data object Submit: ParameterFormEvent()
}