package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet

import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.validation.ValidateName

data class ParameterSetUseCases(
    val getListFlow: GetListFlow,
    val get: Get,
    val getFlow: GetFlow,
    val add: Add,
    val update: Update,
    val validateName: ValidateName,
    val search: Search,
    val deleteList: DeleteList
)
