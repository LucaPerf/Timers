package it.pdm.timers.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import it.pdm.timers.*
import kotlinx.android.synthetic.main.fragment_timer.*

/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {
    private lateinit var userArrayList : ArrayList<Timer>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ntimer = arrayOf("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15")

        val timet = arrayOf("00:01", "00:02", "00:03", "00:04", "00:05", "00:06", "00:07", "00:08", "00:09", "00:10",
            "00:11", "00:12", "00:13", "00:14", "00:15")

        userArrayList = ArrayList()
        for (i in ntimer.indices){
            val timer = Timer(ntimer[i], timet[i])
            userArrayList.add(timer)
        }

        lv_timer.adapter = Adapter(this.requireActivity(), userArrayList)

        /*btn_set_time.setOnClickListener {
            val intent = Intent(this.requireContext(), SetTimeActivity::class.java)
            startActivity(intent)
        }

        btn_avvio.setOnClickListener {
            val intent = Intent(this.requireContext(), CountdownActivity::class.java)
            startActivity(intent)
        }*/

    }

}
