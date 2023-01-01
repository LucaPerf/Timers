package it.pdm.timers.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import it.pdm.timers.*
import it.pdm.timers.R
import it.pdm.timers.Timer
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.android.synthetic.main.fragment_timer_salvati.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

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

    private lateinit var communicator: Communicator

    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_timer, container, false)
        lv_timer = view.findViewById(R.id.listview)

        setrecyclerview()



    /*    //set list
        TimeArrayList = ArrayList()

        communicator = activity as Communicator

        //set recycler_view
        lv_timer = view.findViewById(R.id.listview)

        //set adapter
        TimerAdapter = Adapter(this.requireActivity(), TimeArrayList)

        //set recyclerview adapter
        lv_timer.layoutManager = LinearLayoutManager(this.requireContext())
        lv_timer.adapter = TimerAdapter*/

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
            //addTimers()
            //onRegisterTimer()
            val addTimer = AddTimerFragment()
            createFragment(addTimer)
        }

        fabPlay.setOnClickListener {
            playTimers(mins, secs)
        }

        fabSave.setOnClickListener {
            createRecyclerView()
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
        val v = inflater.inflate(R.layout.add_timers, null)

        val np_minutes = v.findViewById<NumberPicker>(R.id.np_minutes)
        val np_seconds = v.findViewById<NumberPicker>(R.id.np_seconds)

        val r_minutes = v.findViewById<TextView>(R.id.result_minutes)
        val r_seconds = v.findViewById<TextView>(R.id.result_seconds)

        np_minutes.minValue = 0
        np_minutes.maxValue = 60

        np_seconds.minValue = 0
        np_seconds.maxValue = 60

        np_minutes.setOnValueChangedListener { _, _, _ ->
            val minutess = np_minutes.value
            r_minutes.text = String.format("$minutess")
        }

        np_seconds.setOnValueChangedListener { _, _, _ ->
            val secondss = np_seconds.value
            r_seconds.text = String.format("$secondss")
        }

        val addDialog = AlertDialog.Builder(this.requireContext())
        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val min = r_minutes.text.toString()
            val sec = r_seconds.text.toString()

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


    private fun createRecyclerView(){
        communicator.passData("")
    }

    private fun createFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }

    private fun setrecyclerview(){
        val gridLayoutManager = GridLayoutManager(this.requireContext(), 1)
        lv_timer.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(this.requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_loading)
        val dialog = builder.create()
        dialog.show()

        TimeArrayList = ArrayList()
        TimerAdapter = Adapter(this.requireActivity(), TimeArrayList)
        lv_timer.adapter = TimerAdapter

        databaseReference = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Timers")
        dialog.show()

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                TimeArrayList.clear()
                for(itemSnaphot in snapshot.children){
                    val timer = itemSnaphot.getValue(Timer::class.java)
                    if(timer != null){
                        TimeArrayList.add(timer)
                    }
                }
                TimerAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }

        })
    }
}
