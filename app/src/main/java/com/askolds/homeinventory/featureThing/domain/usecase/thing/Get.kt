package com.askolds.homeinventory.featureThing.domain.usecase.thing

import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.domain.model.toThing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Get (
    private val repository: ThingRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(thingId: Int): Thing? {
        return withContext (Dispatchers.IO) {
            return@withContext repository.getById(thingId)?.let { item ->
                val imageUri =
                    if (item.imageId != null)
                        imageRepository.getById(item.imageId)?.imageUri
                    else
                        null
                item.toThing(imageUri)
            }
        }
    }
}