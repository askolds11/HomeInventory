package com.askolds.homeinventory.featureParameter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "parameter",
)
data class ParameterEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val searchName: String,
)