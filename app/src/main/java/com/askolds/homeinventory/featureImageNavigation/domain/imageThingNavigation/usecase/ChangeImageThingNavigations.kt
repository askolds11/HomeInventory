package com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.usecase

import com.askolds.homeinventory.featureImageNavigation.data.repository.ImageThingNavigationRepository
import com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.model.ImageThingNavigation
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
import kotlinx.coroutines.flow.first

class ChangeImageThingNavigations(
    private val repository: ImageThingNavigationRepository,
    private val thingRepository: ThingRepository
) {
    suspend operator fun invoke(thingId: Int, items: List<ImageThingNavigation>) {
        val imageId = thingRepository.getFlowById(thingId).first().imageId!!
        val oldItems = repository.getListByThingId(thingId).first()

        // Get removed items
        // Those are old items that no longer exist
        val removedItems = oldItems.filter { oldItem ->
            !items.any {newItem -> oldItem.id == newItem.id}
        }.map {
            it.id
        }
        // Updated items
        // Items with ids
        val updatedItems = items.filter {
            it.id != 0
        }.map {
            it.toEntity(imageId)
        }
        // New items
        // Items with default ids
        val newItems = items.filter {
            it.id == 0
        }.map {
            it.toEntity(imageId)
        }

        repository.deleteByIds(removedItems)
        repository.updateAll(updatedItems)
        repository.insertAll(newItems)
    }
}