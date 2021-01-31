package com.example.wikimonster.data.model.monster

import com.example.wikimonster.data.model.LocationHabitats


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