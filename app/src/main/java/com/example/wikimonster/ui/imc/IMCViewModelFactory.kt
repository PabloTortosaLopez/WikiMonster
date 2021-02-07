package com.example.wikimonster.ui.imc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wikimonster.data.MonsterDataSource
import com.example.wikimonster.data.MonsterRepository
import com.example.wikimonster.ui.imc.monsters.MonstersViewModel

/**
 * Provider de ViewModel para instanciar IMCViewModel con un IMCViewModel obligatorio con constructor que no esté vacío.
 */
class IMCViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainPageViewModel::class.java)) {
            return MainPageViewModel(
                monsterRepository = MonsterRepository(
                    dataSource = MonsterDataSource()
                )
            ) as T
        }
        if (modelClass.isAssignableFrom(MonstersViewModel::class.java)) {
            return MonstersViewModel(
                monsterRepository = MonsterRepository(
                    dataSource = MonsterDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}