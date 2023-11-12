package com.askolds.homeinventory.featureHome.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "home"
)
data class HomeEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val searchName: String,
)