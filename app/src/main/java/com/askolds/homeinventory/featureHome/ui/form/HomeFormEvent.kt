package com.askolds.homeinventory.featureHome.ui.form

import android.net.Uri

sealed class HomeFormEvent {
    data class NameChanged(val name: String): HomeFormEvent()
    data class ImageChanged(val imageUri: Uri?): HomeFormEvent()
    data object Submit: HomeFormEvent()
}