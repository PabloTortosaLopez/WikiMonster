package com.example.wikimonster.ui.imc

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.wikimonster.R


class MainPagectivity : AppCompatActivity() {

    private lateinit var mainPageViewModel: MainPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_imc)



        val monsters = findViewById<Button>(R.id.monsters_button)
        val weapons = findViewById<Button>(R.id.weapons_button)

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