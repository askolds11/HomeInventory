package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetParameterEntity
import kotlinx.coroutines.flow.Flow

interface ParameterSetParameterRepository {
    suspend fun insertAll(parameterSetParameterList: Collection<ParameterSetParameterEntity>)
    suspend fun getNotListBySetId(parameterSetId: Int): List<ParameterEntity>
    fun getFlowListBySetId(parameterSetId: Int): Flow<List<ParameterEntity>>
    suspend fun getNotListBySetIdAndName(parameterSetId: Int, name: String): List<ParameterEntity>
    suspend fun getListBySetIdAndName(parameterSetId: Int, name: String): List<ParameterEntity>
    suspend fun deleteByIds(ids: Collection<Int>)
    suspend fun deleteByParameterIds(ids: Collection<Int>)

    /**
     * returns map of parameterId -> parameterSetParameterId
     */
    fun getIdsBySetId(parameterSetId: Int): Map<Int, Int>
}