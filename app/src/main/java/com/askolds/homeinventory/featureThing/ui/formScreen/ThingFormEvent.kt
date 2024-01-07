package com.askolds.homeinventory.featureThing.ui.formScreen

import android.net.Uri

sealed class ThingFormEvent {
    data class NameChanged(val name: String): ThingFormEvent()
    data class IsContainerChanged(val isContainer: Boolean): ThingFormEvent()
    data class ImageChanged(val imageUri: Uri?): ThingFormEvent()
    data class ParameterValueChanged(val parameterIndex: Int, val value: String): ThingFormEvent()
    data class ParameterSetParameterValueChanged(
        val parameterSetIndex: Int,
        val parameterSetParameterIndex: Int,
        val value: String
    ): ThingFormEvent()
    data object Submit: ThingFormEvent()
}