package com.askolds.homeinventory.featureParameter.ui.parameterScreen

import com.askolds.homeinventory.featureParameter.domain.model.Parameter

data class ParameterState(
    val parameter: Parameter = Parameter(0, ""),
)