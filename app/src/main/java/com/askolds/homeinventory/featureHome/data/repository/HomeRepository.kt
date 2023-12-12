package com.askolds.homeinventory.featureHome.data.repository

import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun insert(home: HomeEntity): Int
    suspend fun update(home: HomeEntity)
    suspend fun getById(id: Int): HomeEntity?
    fun getFlowById(id: Int): Flow<HomeEntity>
    fun getList(): Flow<List<HomeEntity>>
    fun search(name: String): Flow<List<HomeEntity>>
    suspend fun nameExists(name: String, excludeName: String?): Boolean
    suspend fun deleteByIds(ids: List<Int>)
    suspend fun getImageIdsByIds(ids: List<Int>): List<Int?>
}