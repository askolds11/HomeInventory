package com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet

data class ThingParameterParameterSetUseCases(
    val changeThingParameters: ChangeThingParameters,
    val getListWithoutSetByThingId: GetListWithoutSetByThingId,
    val getListGroupedBySetByThingId: GetListGroupedBySetByThingId
)
