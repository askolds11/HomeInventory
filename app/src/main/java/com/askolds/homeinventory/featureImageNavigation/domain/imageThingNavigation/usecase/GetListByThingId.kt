package com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.usecase

import com.askolds.homeinventory.featureImageNavigation.data.repository.ImageThingNavigationRepository
import com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.model.ImageThingNavigation
import com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.model.toImageThingNavigation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetListByThingId(
    private val imageThingNavigationRepository: ImageThingNavigationRepository
) {
    operator fun invoke(thingId: Int): Flow<List<ImageThingNavigation>> {
        return imageThingNavigationRepository.getListByThingId(thingId)
            .map { list ->
               list.map {
                   it.toImageThingNavigation()
               }
            }
    }
}