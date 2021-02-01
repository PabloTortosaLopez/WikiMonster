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


class MainPagectivity : AppCompatActivity() {

    private lateinit var mainPageViewModel: MainPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_imc)



        val monsters = findViewById<ImageButton>(R.id.monstersButton)
        val weapons = findViewById<ImageButton>(R.id.weaponsButton)

        monsters.setOnClickListener {
            mainPageViewModel.monstersPressed(this)
        }

        weapons.setOnClickListener {
            Toast.makeText(applicationContext,
                "TEST", Toast.LENGTH_SHORT).show()
        }

        mainPageViewModel = ViewModelProviders.of(this, IMCViewModelFactory())
                .get(MainPageViewModel::class.java)

        }

}