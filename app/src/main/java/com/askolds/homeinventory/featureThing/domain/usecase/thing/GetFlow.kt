package com.askolds.homeinventory.featureThing.domain.usecase.thing

import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.domain.model.toThing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFlow (
    private val repository: ThingRepository,
    private val imageRepository: ImageRepository
) {
    operator fun invoke(thingId: Int): Flow<Thing> {
        return repository.getFlowById(thingId).map { item ->
            val imageUri =
                if (item.imageId != null)
                    imageRepository.getById(item.imageId)?.imageUri
                else
                    null
            item.toThing(imageUri)
        }
    }
}