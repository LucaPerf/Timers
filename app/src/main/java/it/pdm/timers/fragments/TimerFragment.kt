package it.pdm.timers.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.pdm.timers.*
import kotlinx.android.synthetic.main.fragment_timer.*

/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {
    private lateinit var userArrayList : ArrayList<Timer>

    private val open : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim) }
    private val close : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim) }

    private var clicked = false

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

        openFunction(view)

    }

    private fun openFunction(view: View) {
        val fab = view?.findViewById<FloatingActionButton>(R.id.fab)
        val fabAdd = view?.findViewById<FloatingActionButton>(R.id.fab_add)
        val fabPlay = view?.findViewById<FloatingActionButton>(R.id.fab_play)
        val fabSave = view?.findViewById<FloatingActionButton>(R.id.fab_save)

        fab.setOnClickListener {
            onAddButtonClicked()
        }

        fabAdd.setOnClickListener {
            val intent = Intent(this.requireContext(), SetTimeActivity::class.java)
            startActivity(intent)
        }

        fabPlay.setOnClickListener {
            val intent = Intent(this.requireContext(), CountdownActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onAddButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean){
        val fab = view?.findViewById<FloatingActionButton>(R.id.fab)
        val fabAdd = view?.findViewById<FloatingActionButton>(R.id.fab_add)
        val fabPlay = view?.findViewById<FloatingActionButton>(R.id.fab_play)
        val fabSave = view?.findViewById<FloatingActionButton>(R.id.fab_save)

        if(!clicked){
            fabAdd?.startAnimation(fromBottom)
            fabPlay?.startAnimation(fromBottom)
            fabSave?.startAnimation(fromBottom)
            fab?.startAnimation(open)
        }else{
            fabAdd?.startAnimation(toBottom)
            fabPlay?.startAnimation(toBottom)
            fabSave?.startAnimation(toBottom)
            fab?.startAnimation(close)
        }
    }

    private fun setVisibility(clicked: Boolean){
        val fabAdd = view?.findViewById<FloatingActionButton>(R.id.fab_add)
        val fabPlay = view?.findViewById<FloatingActionButton>(R.id.fab_play)
        val fabSave = view?.findViewById<FloatingActionButton>(R.id.fab_save)
        if (!clicked){
            fabAdd?.visibility = View.VISIBLE
            fabPlay?.visibility = View.VISIBLE
            fabSave?.visibility = View.VISIBLE
        }else{
            fabAdd?.visibility = View.INVISIBLE
            fabPlay?.visibility = View.INVISIBLE
            fabSave?.visibility = View.INVISIBLE
        }
    }

}
