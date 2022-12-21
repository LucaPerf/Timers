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
import it.pdm.timers.*

/**
 * A simple [Fragment] subclass.
 * Use the [TimerSalvatiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerSalvatiFragment : Fragment() {
    lateinit var newRecyclerView: RecyclerView
    lateinit var newArrayList: ArrayList<Allenamenti>
    lateinit var TimerRecylerViewAllenamenti: AdapterRV

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_timer_salvati, container, false)

        //predisposizione recycler view
        setupRecyclerView(view)

        newArrayList = ArrayList()
        newArrayList.add(Allenamenti("23"))

        newRecyclerView = view.findViewById(R.id.recyclerview)

        TimerRecylerViewAllenamenti = AdapterRV(this.requireActivity(), newArrayList)

        //set recyclerview adapter
        newRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        newRecyclerView.adapter = TimerRecylerViewAllenamenti
        TimerRecylerViewAllenamenti.notifyDataSetChanged()

        getUserdata()
        // Inflate the layout for this fragment
        return view
    }

    private fun setupRecyclerView(view: View){
        newRecyclerView = view.findViewById(R.id.recyclerview)
        //newArrayList = arrayListOf<Allenamenti>()
        newRecyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            newRecyclerView.setHasFixedSize(true)
        }
    }


    private fun getUserdata(){
        TimerRecylerViewAllenamenti.setOnItemClickListner(object : AdapterRV.onItemClickListner{
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), AllenamentoSalvatoActivity::class.java)
                startActivity(intent)
            }
        })


        /* private fun getUserdata() {
       for(i in numbers.indices){
            val allenamenti = Allenamenti(numbers[i])
            newArrayList.add(allenamenti)
        }

     //   var adapter = AdapterRV(newArrayList)
        newRecyclerView.adapter = adapter
        adapter.setOnItemClickListner(object : AdapterRV.onItemClickListner{
            override fun onItemClick(position: Int) {

                val intent = Intent(requireContext(), AllenamentoSalvatoActivity::class.java)
                startActivity(intent)
            }

        })
    }*/

    }
}