package it.pdm.timers.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import it.pdm.timers.*
import it.pdm.timers.R

/**
 * Classe che gestisce gli Allenamenti
 */
class TimerSalvatiFragment : Fragment() {
    private lateinit var AllenamentoArrayList: ArrayList<Allenamenti>
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

        return view
    }

    /**
     * Metodo che permette di gestire la recycler_view della classe
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

        AllenamentoArrayList = ArrayList()
        AllenamentiAdapter = AdapterRV(this.requireActivity(), AllenamentoArrayList)
        recyclerView.adapter = AllenamentiAdapter

        databaseReference = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Allenamenti").child(Firebase.auth.currentUser!!.uid)
        dialog.show()

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                AllenamentoArrayList.clear()
                for(itemSnapshot in snapshot.children){
                    val dataClass = itemSnapshot.getValue(Allenamenti::class.java)
                    if(dataClass != null){
                        AllenamentoArrayList.add(dataClass)
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