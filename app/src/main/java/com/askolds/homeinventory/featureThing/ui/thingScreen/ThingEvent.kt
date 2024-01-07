package com.askolds.homeinventory.featureThing.ui.thingScreen

sealed class ThingEvent {
    data class ParameterValueChanged(val parameterIndex: Int, val value: String): ThingEvent()
    data class ParameterSetParameterValueChanged(
        val parameterSetIndex: Int,
        val parameterSetParameterIndex: Int,
        val value: String
    ): ThingEvent()
}