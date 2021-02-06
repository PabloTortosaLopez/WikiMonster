package com.example.wikimonster.ui.imc.historic

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wikimonster.R
import com.example.wikimonster.data.model.monster.MonsterData
import com.example.wikimonster.ui.imc.IMCViewModelFactory
import kotlinx.android.synthetic.main.monster_tile.view.*

class MonstersActivity : AppCompatActivity() {

    private lateinit var monstersViewModel: MonstersViewModel
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monsters_list)

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

    class MyViewHolder(val monsterTile: CardView) : RecyclerView.ViewHolder(monsterTile)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.monster_tile, parent, false) as CardView

        return MyViewHolder(textView)
    }


    //Func para pintar los datos de cada Historic en su tile
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val monster = monsters[position]

        //Compruebo si la pulsación ha sido larga
        holder.itemView.setOnClickListener {

            listener(monster)



        }

        holder.monsterTile.number.text = monster.id.toString()
        holder.monsterTile.type.text = monster.type
        holder.monsterTile.name.text = monster.name
    }

    override fun getItemCount() = monsters.size
}
