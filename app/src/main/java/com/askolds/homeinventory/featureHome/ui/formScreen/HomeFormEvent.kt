package com.askolds.homeinventory.featureHome.ui.formScreen

import android.net.Uri

sealed class HomeFormEvent {
    data class NameChanged(val name: String): HomeFormEvent()
    data class ImageChanged(val imageUri: Uri?): HomeFormEvent()
    data object Submit: HomeFormEvent()
}