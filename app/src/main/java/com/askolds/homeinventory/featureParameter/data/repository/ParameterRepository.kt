package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import kotlinx.coroutines.flow.Flow

interface ParameterRepository {
    suspend fun insert(parameter: ParameterEntity): Int
    suspend fun update(parameter: ParameterEntity)
//    suspend fun getById(id: Int): HomeEntity?
//    fun getFlowById(id: Int): Flow<HomeEntity>
    fun getList(): Flow<List<ParameterEntity>>
//    fun search(name: String): Flow<List<HomeEntity>>
    suspend fun nameExists(name: String, excludeName: String?): Boolean
    suspend fun deleteByIds(ids: List<Int>)
//    suspend fun getImageIdsByIds(ids: List<Int>): List<Int?>
}