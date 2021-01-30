package com.example.wikimonster.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
//import com.example.imcapp.data.model.HistoricData
import com.example.wikimonster.database.entities.Imc

@Dao
interface ImcDao {
    @Query("SELECT * FROM imc")
    fun getALL(): List<Imc>

    @Insert
    fun insertAll(vararg imcs: Imc)

    @Insert
    fun insert(imc: Imc)

    @Delete
    fun delete(imc: Imc)
}