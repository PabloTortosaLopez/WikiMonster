package com.example.wikimonster.database

import androidx.room.*
import com.example.wikimonster.database.dao.ImcDao
import com.example.wikimonster.database.entities.Imc

@Database(entities = [Imc::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imcDao(): ImcDao
}