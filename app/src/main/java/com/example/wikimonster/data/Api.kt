package com.example.wikimonster.data

import com.example.wikimonster.data.model.MonsterData
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class Api {

    companion object {
        fun buildService(): MonsterService {
            return Retrofit.Builder()
                .baseUrl("https://mhw-db.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(MonsterService::class.java)
        }
    }
}

interface MonsterService {
    @GET("/monsters")
    suspend fun fetchMonsters(): List<MonsterData>
    suspend fun fetchWeapons(): List<String>
}