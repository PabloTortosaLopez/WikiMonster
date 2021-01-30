package com.example.wikimonster.ui.imc.historic

import com.example.wikimonster.data.model.MonsterData
data class MonsterResult(
    val error: Int? = null,
    val monsterResult: List<MonsterData>? = null
)