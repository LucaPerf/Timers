package it.pdm.timers.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.pdm.timers.AdapterRV
import it.pdm.timers.Allenamenti
import it.pdm.timers.AllenamentoSalvatoActivity
import it.pdm.timers.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimerSalvatiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerSalvatiFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Allenamenti>
    lateinit var numbers: Array<String>
    lateinit var allenamenti: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_timer_salvati, container, false)

        numbers = arrayOf(
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19",
            "20"
        )

        newRecyclerView = view.findViewById(R.id.recyclerview)
        newRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Allenamenti>()
        getUserdata()

        // Inflate the layout for this fragment
        return view
    }

    private fun getUserdata() {
        for(i in numbers.indices){
            val allenamenti = Allenamenti(numbers[i])
            newArrayList.add(allenamenti)
        }

        var adapter = AdapterRV(newArrayList)
        newRecyclerView.adapter = adapter
        adapter.setOnItemClickListner(object : AdapterRV.onItemClickListner{
            override fun onItemClick(position: Int) {

                val intent = Intent(requireContext(), AllenamentoSalvatoActivity::class.java)
                intent.putExtra("numbers", newArrayList[position].numbers_recyclerview)
                intent.putExtra("allenamenti", allenamenti[position])
                startActivity(intent)
            }

        })
    }


}