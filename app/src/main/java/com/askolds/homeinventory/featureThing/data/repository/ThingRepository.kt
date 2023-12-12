package com.askolds.homeinventory.featureThing.data.repository

import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import com.askolds.homeinventory.featureThing.data.model.ThingEntity
import kotlinx.coroutines.flow.Flow

interface ThingRepository {
    fun getList(homeId: Int, parentId: Int?): Flow<List<ThingEntity>>
    fun getList(parentId: Int): Flow<List<ThingEntity>>
    suspend fun insert(thing: ThingEntity): Int
    suspend fun update(thing: ThingEntity)
    suspend fun getById(id: Int): ThingEntity?
    fun getFlowById(id: Int): Flow<ThingEntity>
    fun search(homeId: Int?, parentId: Int?, name: String): Flow<List<ThingEntity>>
    suspend fun deleteByIds(ids: List<Int>)
}