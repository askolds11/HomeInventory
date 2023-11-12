package com.askolds.homeinventory.featureHome.ui.home

import com.askolds.homeinventory.featureHome.domain.usecase.validation.ValidateName

sealed class SaveStatus {
    data object None: SaveStatus()
    data object Saving: SaveStatus()
    data object Saved: SaveStatus()
    data object Failed: SaveStatus()
}

data class HomeState (
    val name: String = "",
    val nameValidation: ValidateName.ERROR? = null,
    val saveEnabled: Boolean = false,
    val saveStatus: SaveStatus = SaveStatus.None,
)