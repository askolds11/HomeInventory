package com.askolds.homeinventory.featureParameter.domain.usecase

import com.askolds.homeinventory.featureParameter.domain.usecase.validation.ValidateName

data class ParameterUseCases(
    val getListFlow: GetListFlow,
//    val get: Get,
//    val getFlow: GetFlow,
    val add: Add,
//    val update: Update,
    val validateName: ValidateName,
//    val search: Search,
    val deleteList: DeleteList
)
