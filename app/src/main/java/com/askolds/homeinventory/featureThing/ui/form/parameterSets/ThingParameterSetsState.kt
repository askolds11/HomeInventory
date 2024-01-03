package com.askolds.homeinventory.featureThing.ui.form.parameterSets

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSetListItem

data class ThingParameterSetsState(
    val parameterSetList: SnapshotStateList<ParameterSetListItem> = mutableStateListOf(),
    val query: String = ""
)