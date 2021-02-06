package com.example.wikimonster.ui.imc.historic

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wikimonster.R
import com.example.wikimonster.data.MonsterRepository
//import com.example.imcapp.data.model.HistoricData
import com.example.wikimonster.data.Result
import com.example.wikimonster.data.model.monster.MonsterData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MonstersViewModel(private val monsterRepository: MonsterRepository) : ViewModel() {

    private val _monsterResult = MutableLiveData<MonsterResult>()
    val monstersResult: LiveData<MonsterResult> = _monsterResult

    //Func para recuperar la lista de monsters
  fun retrieveHistoric(ctx: Context) {

        CoroutineScope(Dispatchers.Main).launch {

            val result = monsterRepository.fetchMonsters()

            if (result is Result.Success) {
                _monsterResult.value = MonsterResult(monsterResult = result.data)

            } else {
                _monsterResult.value = MonsterResult(error = R.string.monster_faillure)
            }
        }
    }

    //Func para eliminar el elemento de bbdd local
    fun deleteImc(ctx: Context, monster: MonsterData) {
        Toast.makeText(ctx,
            monster.name, Toast.LENGTH_SHORT).show()
        //TODO: DETALLE VISTA
    }
}