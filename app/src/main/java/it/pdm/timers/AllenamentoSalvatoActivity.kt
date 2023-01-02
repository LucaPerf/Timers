package it.pdm.timers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AllenamentoSalvatoActivity : AppCompatActivity() {
    private lateinit var ArrayTimer : ArrayList<Timer>
    private lateinit var database : DatabaseReference
    private lateinit var adapter : Adapter
    var eventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allenamento_salvato)

        var recyclerview = findViewById<RecyclerView>(R.id.recyclerviews)


        database = FirebaseDatabase.getInstance().getReference("Timers")
        ArrayTimer = ArrayList()
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerview.layoutManager = gridLayoutManager

        adapter = Adapter(this, ArrayTimer)
        recyclerview.adapter = adapter

        eventListener = database!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ArrayTimer.clear()
                for(itemSnaphot in snapshot.children){
                    val timer = itemSnaphot.getValue(Timer::class.java)
                    if(timer != null){
                        ArrayTimer.add(timer)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}