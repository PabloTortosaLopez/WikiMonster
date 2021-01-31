package com.example.wikimonster.data
import com.example.wikimonster.data.model.monster.MonsterData
import kotlinx.coroutines.*

/**
 * Clase que llama a la API y devuelve el el objeto ya creado
 */
class MonsterDataSource {

    suspend fun fetchMonsters() : Result<List<MonsterData>> {

      val monsters = GlobalScope.async {
          return@async Api.buildService().fetchMonsters()
            }

        return Result.Success( monsters.await())
    }
}

