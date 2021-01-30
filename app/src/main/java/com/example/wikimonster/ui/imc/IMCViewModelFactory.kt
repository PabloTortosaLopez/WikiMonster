package com.example.wikimonster.ui.imc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wikimonster.data.IMCDataSource
import com.example.wikimonster.data.IMCRepository
import com.example.wikimonster.ui.imc.historic.HistoricViewModel

/**
 * Provider de ViewModel para instanciar IMCViewModel con un IMCViewModel obligatorio con constructor que no esté vacío.
 */
class IMCViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IMCViewModel::class.java)) {
            return IMCViewModel(
                imcRepository = IMCRepository(
                    dataSource = IMCDataSource()
                )
            ) as T
        }
        if (modelClass.isAssignableFrom(HistoricViewModel::class.java)) {
            return HistoricViewModel(
                imcRepository = IMCRepository(
                    dataSource = IMCDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}