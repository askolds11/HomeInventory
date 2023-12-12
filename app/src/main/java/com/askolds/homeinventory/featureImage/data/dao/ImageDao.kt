package com.askolds.homeinventory.featureImage.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.askolds.homeinventory.featureImage.data.model.ImageEntity

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: ImageEntity): Long
    @Update
    suspend fun update(item: ImageEntity)

    @Query("SELECT * FROM image WHERE id = :id")
    suspend fun getById(id: Int): ImageEntity?

    @Delete
    suspend fun delete(item: ImageEntity)

    @Query("DELETE FROM image WHERE id = :id")
    suspend fun deleteById(id: Int)
}