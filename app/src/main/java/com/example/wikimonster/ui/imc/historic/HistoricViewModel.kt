package com.example.wikimonster.ui.imc.historic

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wikimonster.R
import com.example.wikimonster.data.IMCRepository
//import com.example.imcapp.data.model.HistoricData
import com.example.wikimonster.data.Result
import com.example.wikimonster.data.model.MonsterData
import com.example.wikimonster.database.entities.Imc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoricViewModel(private val imcRepository: IMCRepository) : ViewModel() {

    //private val _historicResult = MutableLiveData<HistoricResult>()
    //val historicResult: LiveData<HistoricResult> = _historicResult

    private val _monsterResult = MutableLiveData<MonsterResult>()
    val monstersResult: LiveData<MonsterResult> = _monsterResult

    //Func para recuperar la lista de Historics
  fun retrieveHistoric(ctx: Context) {

        CoroutineScope(Dispatchers.Main).launch {


            val result = imcRepository.fetchMonsters()
            //val result = imcRepository.retrieveHistoric(ctx = ctx)


            if (result is Result.Success) {
                _monsterResult.value = MonsterResult(monsterResult = result.data)

               // _historicResult.value = HistoricResult(historicResult = result.data)
            } else {
                _monsterResult.value = MonsterResult(error = R.string.monster_faillure)
                //_historicResult.value = HistoricResult(error = R.string.historic_faillure)
            }
        }
    }

    //Func para eliminar el elemento de bbdd local
    fun deleteImc(ctx: Context, monster: MonsterData) {
        CoroutineScope(Dispatchers.Main).launch {

            //val result = imcRepository.deleteImc(ctx = ctx, imc = imc)

            //Si se ha borrado, se recarga la lista de nuevo
           //if (result is Result.Success) {
            //    retrieveHistoric(ctx)
           // } else {
            //    //TODO: FALLO
            //}
        }
    }
}