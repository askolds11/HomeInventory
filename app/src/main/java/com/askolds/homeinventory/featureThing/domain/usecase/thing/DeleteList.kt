package com.askolds.homeinventory.featureThing.domain.usecase.thing

import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository

class DeleteList (
    private val repository: ThingRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(ids: List<Int>) {
        ids.forEach { id ->
            val thing = repository.getById(id)
            if (thing?.imageId != null)
                imageRepository.deleteById(thing.imageId)
        }

        repository.deleteByIds(ids)
    }
}