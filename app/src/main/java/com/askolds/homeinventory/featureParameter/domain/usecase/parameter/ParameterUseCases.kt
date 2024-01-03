package com.askolds.homeinventory.featureParameter.domain.usecase.parameter

import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.validation.ValidateName

data class ParameterUseCases(
    val getList: GetList,
    val getListFlow: GetListFlow,
    val get: Get,
    val getFlow: GetFlow,
    val add: Add,
    val update: Update,
    val validateName: ValidateName,
    val search: Search,
    val deleteList: DeleteList
)
