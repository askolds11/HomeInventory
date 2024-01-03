package com.askolds.homeinventory.featureParameter.ui.parameterSet

import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSet

data class ParameterSetState(
    val parameterSet: ParameterSet = ParameterSet(0, ""),
    val parameters: List<ParameterListItem> = emptyList(),
)