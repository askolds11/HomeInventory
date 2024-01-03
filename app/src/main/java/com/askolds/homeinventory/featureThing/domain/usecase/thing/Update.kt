package com.askolds.homeinventory.featureThing.domain.usecase.thing

import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
import com.askolds.homeinventory.featureThing.domain.ThingConstants
import com.askolds.homeinventory.featureThing.domain.model.Thing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Update (
    private val repository: ThingRepository,
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(thing: Thing, image: Image?) {
        withContext(Dispatchers.IO) {
            val oldThing = repository.getById(thing.id)
            // image changed
            val imageId = if (oldThing?.imageId != thing.imageId) {
                // old image exists, delete
                if (oldThing?.imageId != null)
                    imageRepository.deleteById(oldThing.imageId)


                if (image != null)
                    imageRepository.insert(image.toEntity(), ThingConstants.thingImageSubfolder)
                // new image does not exist
                else
                    null
            } else {
                thing.imageId
            }
            val thingWithImage = thing.copy(imageId = imageId)
            repository.update(thingWithImage.toEntity())
        }
    }
}