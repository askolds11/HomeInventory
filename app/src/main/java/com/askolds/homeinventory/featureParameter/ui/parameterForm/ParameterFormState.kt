package com.askolds.homeinventory.featureParameter.ui.parameterForm

import com.askolds.homeinventory.featureParameter.domain.model.Parameter
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.validation.ValidateName
import com.askolds.homeinventory.ui.SaveStatus

data class ParameterFormState (
    val isEditMode: Boolean = false,
    val parameter: Parameter = Parameter(id = 0, name = ""),
    val nameValidation: ValidateName.ERROR? = null,
    val saveEnabled: Boolean = false,
    val saveStatus: SaveStatus = SaveStatus.None,
)