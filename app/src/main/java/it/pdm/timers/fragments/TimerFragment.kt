package it.pdm.timers.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import it.pdm.timers.CountdownActivity
import it.pdm.timers.R
import it.pdm.timers.SetTimeActivty
import kotlinx.android.synthetic.main.fragment_timer.*

/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_set_time.setOnClickListener {
            val intent = Intent(this.requireContext(), SetTimeActivty::class.java)
            startActivity(intent)
        }

        btn_avvio.setOnClickListener {
            val intent = Intent(this.requireContext(), CountdownActivity::class.java)
            startActivity(intent)
        }

    }

}