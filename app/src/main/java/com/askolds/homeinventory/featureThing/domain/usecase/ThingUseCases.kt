package com.askolds.homeinventory.featureThing.domain.usecase

import com.askolds.homeinventory.featureThing.domain.usecase.validation.ValidateName

data class ThingUseCases(
    val get: Get,
    val getFlow: GetFlow,
    val getListFlow: GetListFlow,
    val add: Add,
    val update: Update,
    val search: Search,
    val deleteList: DeleteList,
    val validateName: ValidateName
)
