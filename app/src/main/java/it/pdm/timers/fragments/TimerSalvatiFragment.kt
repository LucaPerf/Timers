package it.pdm.timers.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import it.pdm.timers.*
import it.pdm.timers.R

/**
 * A simple [Fragment] subclass.
 * Use the [TimerSalvatiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerSalvatiFragment : Fragment() {
    var output : String ?= ""

    private lateinit var newArrayList: ArrayList<Allenamenti>
    private lateinit var TimerRecylerViewAllenamenti: AdapterRV

    private var database : DatabaseReference = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference("Timers")
        .child(Firebase.auth.currentUser!!.uid)
        .child("Timers")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_timer_salvati, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        output = arguments?.getString("message")

        readTimer()
        newArrayList = ArrayList()

        TimerRecylerViewAllenamenti = AdapterRV(this.requireActivity(), newArrayList)

        //set recyclerview adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = TimerRecylerViewAllenamenti
        TimerRecylerViewAllenamenti.notifyDataSetChanged()

        getUserdata()

        return view
    }

    private fun getUserdata() {
        TimerRecylerViewAllenamenti.setOnItemClickListner(object : AdapterRV.onItemClickListner {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), AllenamentoSalvatoActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun readTimer(){
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                newArrayList.clear()
                if(snapshot.exists()){
                    for(data in snapshot.children){
                        val timers = data.getValue(Allenamenti::class.java)
                        newArrayList.add(timers!!)
                    }
                    output?.let { Allenamenti(it) }?.let { newArrayList.add(it) }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TEST", error.message)
            }

        })
    }
}
