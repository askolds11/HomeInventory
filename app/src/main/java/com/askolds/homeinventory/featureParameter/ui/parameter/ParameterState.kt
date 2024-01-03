package com.askolds.homeinventory.featureParameter.ui.parameter

import com.askolds.homeinventory.featureParameter.domain.model.Parameter

data class ParameterState(
    val parameter: Parameter = Parameter(0, ""),
)