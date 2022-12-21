package it.pdm.timers.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.pdm.timers.*
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.android.synthetic.main.fragment_timer_salvati.*
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {
    var mins = 0
    var secs = 0

    private val timerSalvatiFragment = TimerSalvatiFragment()

    private val open : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim) }
    private val close : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim) }

    private var clicked = false

    //recycler view timer
    private lateinit var lv_timer: RecyclerView
    private lateinit var TimeArrayList: ArrayList<Timer>
    private lateinit var TimerAdapter: Adapter

    //recyler view timer salvati
    lateinit var newRecyclerViewList: ArrayList<Allenamenti>
    lateinit var RVAdapter: AdapterRV
    lateinit var rv_timer: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_timer, container, false)

        //set list
        TimeArrayList = ArrayList()
        newRecyclerViewList = ArrayList()

        //set recycler_view
        lv_timer = view.findViewById(R.id.listview)

        //set adapter
        TimerAdapter = Adapter(this.requireActivity(), TimeArrayList)
        RVAdapter = AdapterRV(this.requireActivity(), newRecyclerViewList)

        //set recyclerview adapter
        lv_timer.layoutManager = LinearLayoutManager(this.requireContext())
        lv_timer.adapter = TimerAdapter

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
            //set dialog
            addTimers()
        }

        fabPlay.setOnClickListener {
            playTimers(mins, secs)
        }

        fabSave.setOnClickListener {
            onSavedTimer()
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



    private fun addTimers(){
        val inflater = LayoutInflater.from(this.requireContext())
        val v = inflater.inflate(R.layout.add_item, null)

        val minutes = v.findViewById<EditText>(R.id.minutes)
        val seconds = v.findViewById<EditText>(R.id.seconds)

        val addDialog = AlertDialog.Builder(this.requireContext())
        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val min = minutes.text.toString()
            val sec = seconds.text.toString()

            mins = min.toInt()
            secs = sec.toInt()

            TimeArrayList.add(Timer("$min", "$sec"))
            TimerAdapter.notifyDataSetChanged()
            Log.d(ContentValues.TAG, "Timer aggiunto con successo")
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Log.d(ContentValues.TAG, "Cancel")
        }
        addDialog.create()
        addDialog.show()
    }

    private fun playTimers(min:Int, sec:Int){
        val i =Intent(requireActivity().baseContext, CountdownActivity::class.java)
        i.putExtra("MINUTI", min)
        i.putExtra("SECONDI", sec)
        startActivity(i)
    }


    private fun onSavedTimer(){
        newRecyclerViewList = ArrayList()
        newRecyclerViewList.add(Allenamenti("32"))
        RVAdapter = AdapterRV(this.requireActivity(), newRecyclerViewList)

        Log.d(ContentValues.TAG, "TimerSalvati aggiunto con successo")

        val mini = TimerSalvatiFragment()
        createFragment(mini)
    }

    private fun createFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
}
