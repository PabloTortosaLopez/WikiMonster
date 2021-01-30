package com.example.wikimonster.ui.imc

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wikimonster.data.IMCRepository
import com.example.wikimonster.data.Result

import com.example.wikimonster.R
import com.example.wikimonster.ui.imc.historic.HistoricActivity

class IMCViewModel(private val imcRepository: IMCRepository) : ViewModel() {

    private val _imcForm = MutableLiveData<IMCFormState>()
    val imcFormState: LiveData<IMCFormState> = _imcForm

    private val _imcResult = MutableLiveData<IMCResult>()
    val imcResult: LiveData<IMCResult> = _imcResult

    private val _imcHistoric = MutableLiveData<Void>()
    val imcHistoric: LiveData<Void> = _imcHistoric

    // Gestión del resultado del cálculo del IMC
    fun calculateIMC(username: String, password: String, isMan: Boolean, ctx: Context, save: Boolean) {

        val result = imcRepository.calculateIMC(username, password, isMan, ctx = ctx, save = save)

        if (result is Result.Success) {
            _imcResult.value = IMCResult(imcCalculated = IMCCalculatedView(imcValue = result.data.value, imcState = result.data.state))
        } else {
            _imcResult.value = IMCResult(error = R.string.calculation_failed)
        }
    }

    // Gestionar si alguno de los campos está vacío
    fun imcDataChanged(weight: String, height: String) {
        if (!isFieldEmpty(weight)) {
            _imcForm.value = IMCFormState(weightError = R.string.empty_weight)
        } else if (!isFieldEmpty(height)) {
            _imcForm.value = IMCFormState(heightError = R.string.empty_height)
        } else {
            _imcForm.value = IMCFormState(isDataValid = true)
        }
    }

    fun historicPressed(context: Context) {
        val intent = Intent(context, HistoricActivity::class.java)
        startActivity(context,intent, null)
    }


    // Función para comprobar un texto está vacío
    private fun isFieldEmpty( string: String) : Boolean {
        return string.isNotBlank()
    }
}