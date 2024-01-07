package com.askolds.homeinventory.featureImageNavigation.data.repository

import com.askolds.homeinventory.featureImageNavigation.data.model.ImageThingNavigationEntity
import kotlinx.coroutines.flow.Flow

interface ImageThingNavigationRepository {
    suspend fun insertAll(items: List<ImageThingNavigationEntity>)
    suspend fun updateAll(items: List<ImageThingNavigationEntity>)
    fun getListByThingId(thingId: Int): Flow<List<ImageThingNavigationEntity>>
    suspend fun deleteByIds(ids: List<Int>)
}