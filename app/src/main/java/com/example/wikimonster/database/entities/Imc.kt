package com.example.wikimonster.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Imc(
    @PrimaryKey(autoGenerate = true) val imcId: Int = 0,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "value") val value: Double?,
    @ColumnInfo(name = "state") val state: String?,
    @ColumnInfo(name = "height") val height: String?,
    @ColumnInfo(name = "weight") val weight: String?
)