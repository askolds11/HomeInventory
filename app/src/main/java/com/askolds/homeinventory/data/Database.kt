package com.askolds.homeinventory.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.askolds.homeinventory.featureHome.data.dao.HomeDao
import com.askolds.homeinventory.featureHome.data.model.HomeEntity

@Database(
    entities = [
        HomeEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class Database: RoomDatabase() {
    abstract fun homeDao(): HomeDao

    companion object {
        const val DATABASE_NAME = "home_inventory_db"
    }
}