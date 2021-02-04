package com.example.wikimonster.ui.imc.historic

import android.app.AlertDialog
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wikimonster.R
import com.example.wikimonster.data.model.monster.MonsterData
//import com.example.imcapp.data.model.HistoricData
import com.example.wikimonster.ui.imc.IMCViewModelFactory
import kotlinx.android.synthetic.main.historic_tile.view.*

class MonstersActivity : AppCompatActivity() {

    private lateinit var monstersViewModel: MonstersViewModel
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic)

        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = "Monsters"

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "New Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Instanciar viewmodel
        monstersViewModel = ViewModelProviders.of(this, IMCViewModelFactory()).get(MonstersViewModel::class.java)

        imageView = findViewById(R.id.loadingWidget)
        Glide.with(applicationContext).asGif()
            .load(R.drawable.diablos_loading)
            .into(imageView)
        //Lamada a la funcion del viewmodel para recuperar historics
        monstersViewModel.retrieveHistoric(this)


        //Escuchar los estados del viewmodel
        monstersViewModel.monstersResult.observe(this@MonstersActivity, Observer {
            val historicState = it ?: return@Observer


            if (historicState.monsterResult != null) {
                imageView.visibility = View.GONE
                //Instanciar el adapter para la recyclerview
                viewAdapter = HistoricAdapter(monsters = historicState.monsterResult) { monster -> monstersViewModel.deleteImc(this, monster)}
               // viewManager = LinearLayoutManager(this)
                viewManager = GridLayoutManager(this,2)
                findViewById<RecyclerView>(R.id.recyclerView).apply {
                    setHasFixedSize(true)

                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
            if (historicState.error != null) {
                //TODO GESTIONAR ERROR O LISTA VACIA
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

class HistoricAdapter(private val monsters: List<MonsterData>, private val listener: (MonsterData) -> Unit) : RecyclerView.Adapter<HistoricAdapter.MyViewHolder>() {

    class MyViewHolder(val historicTile: ConstraintLayout) : RecyclerView.ViewHolder(historicTile)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.historic_tile, parent, false) as ConstraintLayout

        return MyViewHolder(textView)
    }


    //Func para pintar los datos de cada Historic en su tile
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val monster = monsters[position]

        //Compruebo si la pulsación ha sido larga
        holder.itemView.setOnLongClickListener {

            //Se muestra dialog preguntando si desea borrar el elemento
            val dialog = AlertDialog.Builder(holder.historicTile.rootView.context)
                .setTitle(holder.historicTile.rootView.context.getString(R.string.alert_title_delete))
                .setNegativeButton(holder.historicTile.rootView.context.getString(R.string.negative_alert_button)) { view, _ ->
                    //Si cancela, no pasa nada
                }
                .setPositiveButton(holder.historicTile.rootView.context.getString(R.string.positive_alert_button)) { view, _ ->
                    //Si acepta, se manda el imc a través del listener a la actividad para gestionar su borrado
                    listener(monster)
                }
                .setCancelable(false)
                .create()

            dialog.show()

            true

        }

        //val historicsDate = historics[position].date?.split("-")?.toTypedArray()
        //var monthString = ""

        //Sacar el nombre del mes a partir de su numero



        holder.historicTile.month.text = monster.name
        holder.historicTile.year.text = monster.type
        holder.historicTile.day.text =""

        holder.historicTile.gender.text = ""
        holder.historicTile.state.text = ""

        holder.historicTile.tv_height.text = ""
        holder.historicTile.tv_weight.text = ""


        holder.historicTile.value.text = ""
    }

    override fun getItemCount() = monsters.size
}
