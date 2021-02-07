package com.example.wikimonster.ui.imc.monsters

import com.example.wikimonster.data.model.monster.MonsterData
data class MonsterResult(
    val error: Int? = null,
    val monsterResult: List<MonsterData>? = null
)