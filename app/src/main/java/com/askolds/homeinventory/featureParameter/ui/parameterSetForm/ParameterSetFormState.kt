package com.askolds.homeinventory.featureParameter.ui.parameterSetForm

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSet
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.validation.ValidateName
import com.askolds.homeinventory.core.ui.SaveStatus

data class ParameterSetFormState (
    val isEditMode: Boolean = false,
    val parameterSet: ParameterSet = ParameterSet(id = 0, name = ""),
    // Seems like copying the item inside a list is slow (noticeable small delay when selecting items using short click)
    // have to use mutable state list
    val parameterList: SnapshotStateList<ParameterListItem> = mutableStateListOf(),
    val nameValidation: ValidateName.ERROR? = null,
    val saveEnabled: Boolean = false,
    val saveStatus: SaveStatus = SaveStatus.None,
)