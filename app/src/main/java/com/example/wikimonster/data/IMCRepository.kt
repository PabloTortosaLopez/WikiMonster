package com.example.wikimonster.data

import android.content.Context
//import com.example.imcapp.data.model.HistoricData
import com.example.wikimonster.data.model.IMCData
import com.example.wikimonster.data.model.MonsterData
import com.example.wikimonster.database.entities.Imc

/**
 * Clase que almacena las funciones relacionadas con el cálculo del IMC y utiliza un dataSource para realizar llamadas a servidor
 */

class IMCRepository(val dataSource: IMCDataSource) {

    fun calculateIMC(weight: String, height: String, isMan: Boolean, ctx: Context, save: Boolean): Result<IMCData> {
        return dataSource.calculateIMC(height, weight, isMan, ctx, save)
    }

  suspend fun retrieveHistoric(ctx: Context) : Result<List<Imc>> {
        return dataSource.retrieveHistoric(ctx)
    }

    suspend fun fetchMonsters() : Result<List<MonsterData>> {
        return dataSource.fetchMonsters()
    }
}