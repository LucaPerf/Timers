package it.pdm.timers.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import it.pdm.timers.*
import it.pdm.timers.R
import kotlinx.android.synthetic.main.activity_allenamento_salvato.*

/**
 * Classe che permette la gestione del fragment TimerSalvatiFragment
 */
class TimerSalvatiFragment : Fragment() {
    var output: String? = ""

    private lateinit var newArrayList: ArrayList<Allenamenti>
    private lateinit var AllenamentiAdapter: AdapterRV
    private lateinit var recyclerView: RecyclerView
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_timer_salvati, container, false)
        recyclerView = view.findViewById(R.id.recyclerview)

        setrecyclerview()
        /*  output = arguments?.getString("message")

        //  readTimer()
        newArrayList = ArrayList()

        TimerRecylerViewAllenamenti = AdapterRV(this.requireActivity(), newArrayList)

        //set recyclerview adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = TimerRecylerViewAllenamenti
        TimerRecylerViewAllenamenti.notifyDataSetChanged()

        getUserdata()
*/
        return view
    }

    /**
     * Metodo che permette la gestione della recycler_view
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setrecyclerview(){
        val gridLayoutManager = GridLayoutManager(this.requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(this.requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_loading)
        val dialog = builder.create()
        dialog.show()

        newArrayList = ArrayList()
        AllenamentiAdapter = AdapterRV(this.requireActivity(), newArrayList)
        recyclerView.adapter = AllenamentiAdapter

        databaseReference = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Allenamenti").child(Firebase.auth.currentUser!!.uid)
        dialog.show()

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                newArrayList.clear()
                for(itemSnapshot in snapshot.children){
                    val dataClass = itemSnapshot.getValue(Allenamenti::class.java)
                    if(dataClass != null){
                        newArrayList.add(dataClass)
                    }
                }
                AllenamentiAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }

        })
    }
}

  /*  private fun getUserdata() {
        TimerRecylerViewAllenamenti.setOnItemClickListner(object : AdapterRV.onItemClickListner {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), AllenamentoSalvatoActivity::class.java)
                startActivity(intent)
            }
        })
    }
}*/

 /*   private fun readTimer(){
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
    }*/