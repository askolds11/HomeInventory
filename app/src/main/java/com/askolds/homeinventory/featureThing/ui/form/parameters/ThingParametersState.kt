package com.askolds.homeinventory.featureThing.ui.form.parameters

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem

data class ThingParametersState(
    val parameterList: SnapshotStateList<ParameterListItem> = mutableStateListOf(),
    val query: String = ""
)