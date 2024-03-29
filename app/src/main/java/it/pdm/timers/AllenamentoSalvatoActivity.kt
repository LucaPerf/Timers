package it.pdm.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

/**
 * Classe che permette la gestione di un allenamento salvato
 */
class AllenamentoSalvatoActivity : AppCompatActivity(){
    lateinit var ArrayListTimer: ArrayList<Timer>
    private lateinit var recyclerview: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var adapter: Adapter
    var eventListener: ValueEventListener? = null
    private var min = 0
    private var sec = 0
    private var sum = 0
    private var currentTimer = 0
    private var size = 0
    private var number_path = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allenamento_salvato)

        recyclerview = findViewById(R.id.recyclerviews)

        val button_avvio = findViewById<Button>(R.id.btn_avvio)

        readAllenamento()

        button_avvio.setOnClickListener {
            readTimers()
        }

    }

    /**
     * Metodo che permette di leggere i Timer contenuti in un apposito path di Firebase, e scriverli nell'ArrayList
     */
    private fun readAllenamento() {
        val intent = intent
        val number = intent.getStringExtra("Timers")
        number_path = intent.getStringExtra("Timers").toString()
        if (number != null) {
            Log.e("number: ", number)
        }

        database = FirebaseDatabase.getInstance().getReference("Timers")
            .child(Firebase.auth.currentUser!!.uid).child("Allenamento " + number)

        ArrayListTimer = ArrayList()
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerview.layoutManager = gridLayoutManager

        adapter = Adapter(this, ArrayListTimer)
        recyclerview.adapter = adapter

        eventListener = database!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ArrayListTimer.clear()
                for (itemSnaphot in snapshot.children) {
                    val timer = itemSnaphot.getValue(Timer::class.java)
                    if (timer != null) {
                        ArrayListTimer.add(timer)
                        size = ArrayListTimer.size
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", "error")
            }

        })
    }

    /**
     * Metodo che permette di leggere i Timer contenuti nell'ArrayList
     */
    private fun readTimers() {
        if(ArrayListTimer.isEmpty()){
            Toast.makeText(this, "Non ci sono timer", Toast.LENGTH_SHORT).show()
        }else{
            if (size != 0){
                val array = ArrayListTimer.get(currentTimer)

                val minuti = array.minuti.toString()
                min = minuti.toInt()

                val secondi = array.secondi.toString()
                sec = secondi.toInt()

                sum = (min * 60 * 1000) + (sec * 1000) + 1000

                playTimers(min, sec)
                currentTimer += 1

                val timer = java.util.Timer()

                val timerTask = object  : TimerTask(){
                    override fun run() {
                        if(size > 1){
                            size -= 1
                            playAlarm()
                            readTimers()
                        }else{
                            size -= 1
                            playAlarmFinish()
                            readTimers()
                        }
                    }
                }
                timer.schedule(timerTask, sum.toLong())
            }else{
                returnAllenamento()
            }
        }
    }

    /**
     * Metodo che permette di aprire la classe CountdownActivity
     */
    private fun playTimers(min: Int, sec: Int) {
        val intent = Intent(this.baseContext, CountdownActivity::class.java)
        intent.putExtra("MINUTI", min)
        intent.putExtra("SECONDI", sec)
        startActivity(intent)
    }

    /**
     * Metodo che permette di ritornare all'activity di partenza
     */
    private fun returnAllenamento() {
        val intent = Intent(this, TimerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    /**
     * Metodo che apre la classe BackgroundMusicService
     */
    private fun playAlarm(){
        val intent = Intent(this, BackgroundMusicService::class.java)
        startService(intent)
    }

    /**
     * Metodo che apre la classe BackgroundAlarmFinishService
     */
    private fun playAlarmFinish(){
        val intent = Intent(this, BackgroundAlarmFinishService::class.java)
        startService(intent)
    }
}
