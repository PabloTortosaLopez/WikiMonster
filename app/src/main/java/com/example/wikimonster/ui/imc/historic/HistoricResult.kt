package com.example.wikimonster.ui.imc.historic

//import com.example.imcapp.data.model.HistoricData
import com.example.wikimonster.database.entities.Imc

// Estados del historico
data class HistoricResult(
    val error: Int? = null,
    val historicResult: List<Imc>? = null
)