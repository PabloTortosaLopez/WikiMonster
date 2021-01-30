package com.example.wikimonster.data.model


/**
 * Clase que almacena los datos recogidos para cada Monster de la API
 */

data class MonsterData(
    val id: Int,
    val name: String,
    val type: String,
    val species: String,
    val description: String,
    val locations: List<LocationHabitats>?,
    val resistances: List<Resistances>?,
    val weaknesses: List<Weaknesses>?
)

//data class HistoricData(
  //  val date: String,
    //val gender: String,
    //val value: Double,
    //val state: String,
    //val height: String,
    //val weight: String
//)