package com.askolds.homeinventory.featureThing.domain.usecase

import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
import com.askolds.homeinventory.featureThing.domain.ThingConstants
import com.askolds.homeinventory.featureThing.domain.model.Thing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Add (
    private val repository: ThingRepository,
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(thing: Thing, image: Image?): Int {
        return withContext(Dispatchers.IO) {
            val imageId =
                if (image != null)
                    imageRepository.insert(image.toEntity(), ThingConstants.thingImageSubfolder)
                else
                    null
            val thingWithImage = thing.copy(imageId = imageId)
            return@withContext repository.insert(thingWithImage.toEntity())
        }
    }
}