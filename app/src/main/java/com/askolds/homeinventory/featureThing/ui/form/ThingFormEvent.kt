package com.askolds.homeinventory.featureThing.ui.form

import android.net.Uri

sealed class ThingFormEvent {
    data class NameChanged(val name: String): ThingFormEvent()
    data class IsContainerChanged(val isContainer: Boolean): ThingFormEvent()
    data class ImageChanged(val imageUri: Uri?): ThingFormEvent()
    data object Submit: ThingFormEvent()
}