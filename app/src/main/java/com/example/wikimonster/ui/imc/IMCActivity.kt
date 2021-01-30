package com.example.wikimonster.ui.imc

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.wikimonster.R
import com.google.android.material.snackbar.Snackbar


class IMCActivity : AppCompatActivity() {

    private lateinit var imcViewModel: IMCViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_imc)

        val weight = findViewById<EditText>(R.id.tv_weight)
        val height = findViewById<EditText>(R.id.tv_height)
        val calculate = findViewById<Button>(R.id.calculate)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val imcValue = findViewById<TextView>(R.id.imcText)
        val imcState = findViewById<TextView>(R.id.imcStateText)
        val radioGroup = findViewById<RadioGroup>(R.id.myRadioGroup)
        val historic = findViewById<Button>(R.id.historic)
        var isMan = true

        historic.setOnClickListener {
            imcViewModel.historicPressed(this)
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId -> // Averiguar qué radioButton está seleccionado
            when (checkedId) {
                R.id.men -> {
                    isMan = true
                }
                R.id.women -> {
                    isMan = false
                }
            }
        }

        imcViewModel = ViewModelProviders.of(this, IMCViewModelFactory())
                .get(IMCViewModel::class.java)

        imcViewModel.imcFormState.observe(this@IMCActivity, Observer {
            val imcState = it ?: return@Observer

            // Deshabilitar botón de calculo hasta que el peso y la altura estén rellenos
            calculate.isEnabled = imcState.isDataValid

            if (imcState.weightError != null) {
                weight.error = getString(imcState.weightError)
            }
            if (imcState.heightError != null) {
                height.error = getString(imcState.heightError)
            }
        })

        // Gestionar el estado del ViewModel
        imcViewModel.imcResult.observe(this@IMCActivity, Observer {
            val imcResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (imcResult.error != null) {
                showIMCFailed(imcResult.error)
            }
            if (imcResult.imcCalculated != null) {
                imcValue.text = String.format("%.2f", imcResult.imcCalculated.imcValue)
                imcState.text = imcResult.imcCalculated.imcState
            }
            setResult(Activity.RESULT_OK)
        })

        weight.afterTextChanged {
            imcViewModel.imcDataChanged(
                    weight.text.toString(),
                    height.text.toString()
            )
        }

        height.apply {
            afterTextChanged {
                imcViewModel.imcDataChanged(
                        weight.text.toString(),
                        height.text.toString()
                )
            }

            calculate.setOnClickListener {
                loading.visibility = View.VISIBLE
                val dialog = AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.alert_title_save))
                    .setNegativeButton(context.getString(R.string.negative_alert_button)) { view, _ ->
                        manageIMCData(view, weight, height, isMan, context.getString(R.string.alert_negative_info), save = false)

                    }
                    .setPositiveButton(context.getString(R.string.positive_alert_button)) { view, _ ->

                        manageIMCData(view, weight, height, isMan, context.getString(R.string.alert_positive_info), save = true)
                    }
                    .setCancelable(false)
                    .create()

                dialog.show()

            }
        }
    }

    private fun EditText.manageIMCData(
        view: DialogInterface,
        weight: EditText,
        height: EditText,
        isMan: Boolean,
        message: String,
        save: Boolean
    ) {
        //Guardar teclado
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

        Snackbar.make(
            this,
            message,
            Snackbar.LENGTH_LONG
        ).show()

        view.dismiss()
        //Se llama al método del viewmodel para calcular el imc y almacenarlo o no en función de lo elegido
        imcViewModel.calculateIMC(
            weight.text.toString(), height.text.toString(), isMan = isMan, ctx = context,
            save = save
        )
    }

    private fun showIMCFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

}

/**
 * Función de extension para simplificar afetTextChanged para los EditText
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}