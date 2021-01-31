package com.example.wikimonster.data

//import com.example.imcapp.data.model.HistoricData
import com.example.wikimonster.data.model.monster.MonsterData

/**
 * Clase que almacena las funciones relacionadas con las llamadas a la API y utiliza un dataSource para realizarlas
 */

class MonsterRepository(val dataSource: MonsterDataSource) {

    suspend fun fetchMonsters() : Result<List<MonsterData>> {
        return dataSource.fetchMonsters()
    }
}