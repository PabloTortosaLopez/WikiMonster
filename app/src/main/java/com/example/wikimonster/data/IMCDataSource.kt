package com.example.wikimonster.data
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.room.Room
//import com.example.imcapp.data.model.HistoricData
import com.example.wikimonster.data.model.IMCData
import com.example.wikimonster.data.model.MonsterData
import com.example.wikimonster.database.AppDatabase
import com.example.wikimonster.database.entities.Imc
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.io.*
import java.lang.Exception
import java.util.*


/**
 * Clase que simula llamada a servidor para obtener los datos del IMC, los almacena en la bbdd local y los recupera
 */
class IMCDataSource {

    fun calculateIMC(height: String, weight: String, isMan: Boolean, ctx: Context, save: Boolean): Result<IMCData> {
        return try {
            Result.Success(fakeIMCCalculation(height, weight, isMan, ctx, save))
        } catch (e: Throwable) {
            Result.Error(IOException("Error calculating IMC", e))
        }
    }

    suspend fun retrieveHistoric(ctx: Context): Result<List<Imc>> {
        return try {
            Result.Success(retrieveHistoricData(ctx))

        } catch (e: Throwable) {
            Result.Error(IOException("Error retrieveing Historic", e))
        }
    }

    private suspend fun retrieveHistoricData(ctx: Context) : List<Imc> {
        //Se recuperan los historicos del archivo de texto

        return readFromDB(ctx)

        //return readFile(ctx)
    }

    private fun fakeIMCCalculation(height: String, weight: String, isMan: Boolean, ctx: Context, save: Boolean) : IMCData {
        val value = getIMCValue(height, weight)
        val state = getIMCState(value, isMan)

        val imcData = IMCData(value, state)

        if (save) {



            saveInDB(imcData, ctx, isMan, height, weight)



            //writeFile(imcData, ctx, isMan, height, weight)
        }

        return imcData
    }

    private fun getIMCValue(height: String, weight: String) : Double {
        return weight.toDouble() / ((height.toDouble() * height.toDouble())/10000)
    }

    private fun getIMCState(value: Double, isMan: Boolean) : String {

        val normalMinimum = 18.5
        val normalLimit: Double
        val overWeightMinimum: Double
        val overWeightLimit: Double

        if (isMan) {
            normalLimit = 24.9
            overWeightMinimum = 25.0
            overWeightLimit = 29.9
        } else {
            normalLimit = 23.9
            overWeightMinimum = 24.0
            overWeightLimit = 28.9
        }

        return when {
            value < normalMinimum -> "Inferior al normal"
            value in normalMinimum..normalLimit -> "Normal"
            value in overWeightMinimum..overWeightLimit -> "Sobrepeso"
            else -> "Obesidad"
        }
    }

    private fun saveInDB(datos: IMCData, ctx: Context, isMan: Boolean, height: String, weight: String) {
        try {

            //Se abre un hilo diferente al main
           GlobalScope.launch(Dispatchers.IO) {


               //Se abre la conexion a la bbdd
               val db = Room.databaseBuilder(
                   ctx,
                   AppDatabase::class.java, "database-name"
               ).build()

               val hoy = Calendar.getInstance()

               val date = hoy.get(Calendar.DAY_OF_MONTH)
                   .toString() + "-" + (hoy.get(Calendar.MONTH) + 1) + "-" + hoy.get(Calendar.YEAR)

               val gender = if (isMan) "Hombre" else "Mujer"

               //Se crea el objecto IMC para almacenar en la bddd
               val imcToStore = Imc(
                   date = date,
                   gender = gender,
                   value = datos.value,
                   state = datos.state,
                   height = height,
                   weight = weight
               )

               //Se almacena en la bbdd
               db.imcDao().insert(imcToStore)

              return@launch

           }

        } catch (e: Exception) {
            print(message = e.message)
        }


    }

    private fun writeFile(datos: IMCData, ctx: Context, isMan: Boolean, height: String, weight: String) {

        try {
            // Si el fichero no existe se crea,
        // si existe se añade la información.

            val salida: OutputStreamWriter =
                OutputStreamWriter(
                    ctx.openFileOutput(
                "imcAlmacenados.txt",
                Activity.MODE_APPEND)
        )

           val hoy = Calendar.getInstance()

            val gender = if (isMan) "Hombre" else "Mujer"

        // Se escribe en el fichero línea a línea.
            salida.write(hoy.get(Calendar.DAY_OF_MONTH).toString() + "-" + (hoy.get(Calendar.MONTH) + 1) + "-" + hoy.get(Calendar.YEAR) + ";" + gender + ";" + datos.value + ";" + datos.state + ";" + height + ";" + weight + '\n')

        // Se confirma la escritura.
        salida.flush()
        salida.close()


           } catch (e: IOException) {
            Log.ERROR
         }}


    private suspend fun readFromDB(ctx: Context) : List<Imc> {

        //Se abre un hilo asíncrono
        var imcListasync = GlobalScope.async {

        //Se abre la conexion a la bbdd
        val db = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java, "database-name"
        ).build()

            //Se recuperan todos los elementos de la bbdd
            return@async db.imcDao().getALL().toMutableList();


    }
        //Se saca el resultado
        return imcListasync.await()
    }

    private fun readFile(ctx: Context) : List<Imc> {

        //Se crea una lista vacia
        val historicList: MutableList<Imc> = mutableListOf()

        //Si existe el fichero se recorre y por cada linea se almacena en la lista un HistoricData
        if (ctx.fileList().contains("imcAlmacenados.txt")) {
            print(message = "")

            try {
                val entrada = InputStreamReader(ctx.openFileInput("imcAlmacenados.txt"))
                val br = BufferedReader(entrada)
                var linea = br.readLine()

                //Se lee cada linea
                while (!linea.isNullOrEmpty()) {
                    print(message = "$linea\n")

                    //Se separa la linea  por ;
                    val historicDataLine = linea.split(";").toTypedArray()
                    //Se crea un Historic data por cada elemento separado
                    historicList.add(Imc(date = historicDataLine[0], gender = historicDataLine[1], value = historicDataLine[2].toDouble(), state = historicDataLine[3], height = historicDataLine[4], weight = historicDataLine[5]))
                    linea = br.readLine()
                }

                //Se confirma la lectura
                br.close()
                entrada.close()
            } catch (e: IOException) {
                print(message = e.message)
            }

        } else {
            print(message = "Error")
        }
        return historicList
    }

    suspend fun fetchMonsters() : Result<List<MonsterData>> {

      val monsters = GlobalScope.async {
            val service = Retrofit.Builder()
                .baseUrl("https://mhw-db.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(MonsterService::class.java)

                return@async service.fetchMonsters()
            }

        return Result.Success( monsters.await())
    }
}

interface MonsterService {
    @GET("/monsters")
    suspend fun fetchMonsters(): List<MonsterData>
}

data class MonsterResponse(val result: MonsterData)