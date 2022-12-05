package it.pdm.timers.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.pdm.timers.*
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {
    lateinit var lv_timer: ListView
    lateinit var TimeArrayList : ArrayList<Timer>

    private val open : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim) }
    private val close : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim) }

    private var clicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_timer, container, false)
        popolateListView(view)
        // Inflate the layout for this fragment
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //predisposizione list view
        //popolateListView(view)

        //apertura del floating action button
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
            /*val intent = Intent(this.requireContext(), SetTimeActivity::class.java)
            startActivity(intent)*/

            val add_timer = add_timer()
            createFragment(add_timer)
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

    private fun popolateListView(view: View) {
        lv_timer = view?.findViewById(R.id.listview)

        TimeArrayList = ArrayList()
        //lv_timer.adapter = Adapter(this.requireActivity(), TimeArrayList)

        val timer = Timer("1", "1", "4", "3")
        TimeArrayList.add(timer)

        lv_timer.adapter = Adapter(this.requireActivity(), TimeArrayList)

        /*lv_timer = view.findViewById(R.id.listview)
        TimeArrayList = arrayListOf<Timer>()

        /*val time = Timer(addTimer.min2, addTimer.min1, addTimer.sec2, addTimer.sec1)
        TimeArrayList.add(time)*/
        lv_timer.adapter = Adapter(this.requireActivity(), TimeArrayList)*/
    }

    fun getList(): ArrayList<Timer> {
        return TimeArrayList
    }

    fun getLV(): ListView{
        return lv_timer
    }

    private fun createFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
}
