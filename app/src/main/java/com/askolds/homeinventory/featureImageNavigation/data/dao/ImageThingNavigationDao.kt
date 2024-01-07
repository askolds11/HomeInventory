package com.askolds.homeinventory.featureImageNavigation.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.askolds.homeinventory.featureImageNavigation.data.model.ImageThingNavigationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageThingNavigationDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(items: List<ImageThingNavigationEntity>)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateAll(items: List<ImageThingNavigationEntity>)

    @Query(
        """
        SELECT image_thing_navigation.*
        FROM thing
        INNER JOIN image_thing_navigation ON image_thing_navigation.imageId = thing.imageId
        WHERE thing.id = :thingId
        ORDER BY image_thing_navigation.zIndex ASC
        """
    )
    fun getListByThingId(thingId: Int): Flow<List<ImageThingNavigationEntity>>

    @Query("DELETE FROM image_thing_navigation WHERE id in (:ids)")
    suspend fun deleteByIds(ids: List<Int>)
}