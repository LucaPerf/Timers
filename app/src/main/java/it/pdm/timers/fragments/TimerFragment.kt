package it.pdm.timers.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.childEvents
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import it.pdm.timers.*
import it.pdm.timers.R
import it.pdm.timers.Timer
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.android.synthetic.main.fragment_timer_salvati.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {
    private var min = 0
    private var sec = 0
    private var sum = 0
    private var currentTimer = 0
    private var size = 0

    var number_path = 0

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
    var databaseReference2: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    var eventListener2: ValueEventListener? = null
    var query: Query? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_timer, container, false)
        lv_timer = view.findViewById(R.id.listview)
        communicator = activity as Communicator

        setrecyclerview()

        val intent = Intent(this.requireContext(), AllenamentoSalvatoActivity::class.java)
        intent.putExtra("path", number_path.toString())

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //predisposizione list view
        //popolateListView(view)
        //apertura del floating action button

        openFunction(view)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
            createRecyclerView()
            /*val addTimer = AddTimerFragment()
            createFragment(addTimer)*/
        }

        fabPlay.setOnClickListener {
            readTimers()
        }

        fabSave.setOnClickListener {
            changePath()
            saveData()
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

    private fun addTimer(){
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

    private fun createRecyclerView(){
        communicator.passData(number_path)
    }

    private fun createFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }

    fun setrecyclerview(){
        databaseReference = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Timers").child(Firebase.auth.currentUser!!.uid)

        query = databaseReference?.orderByChild(number_path.toString())?.limitToLast(1)

        val gridLayoutManager = GridLayoutManager(this.requireContext(), 1)
        lv_timer.layoutManager = gridLayoutManager
        Log.e("path", number_path.toString())

        val builder = AlertDialog.Builder(this.requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_loading)
        val dialog = builder.create()
        dialog.show()

        TimeArrayList = ArrayList()
        TimerAdapter = Adapter(this.requireActivity(), TimeArrayList)
        lv_timer.adapter = TimerAdapter

        eventListener = query!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //number_path = snapshot.childrenCount.toInt()

                val np = number_path
                Log.e("np", np.toString())

                databaseReference2 = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("Timers").child(Firebase.auth.currentUser!!.uid).child(np.toString())

                eventListener2 = databaseReference2?.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        TimeArrayList.clear()
                        for(itemSnaphot in snapshot.children){
                            val timer = itemSnaphot.getValue(Timer::class.java)
                            if(timer != null){
                                TimeArrayList.add(timer)
                                size = TimeArrayList.size
                            }
                        }
                        TimerAdapter.notifyDataSetChanged()
                        dialog.dismiss()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun saveData(){
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_loading)
        val dialog = builder.create()
        dialog.show()
        uploadData()
        dialog.dismiss()
    }

    private fun uploadData(){
        val dataClass = Allenamenti(listview.toString())
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Allenamenti").child(Firebase.auth.currentUser!!.uid).child(currentDate)
            .setValue(dataClass).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this.requireContext(), "salvato", Toast.LENGTH_SHORT).show()
                   // createRecyclerView()
                    createFragment(timerSalvatiFragment)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this.requireContext(), "errore", Toast.LENGTH_SHORT).show()
            }
    }

    private fun readTimers() {
        if (size != 0){
            val array = TimeArrayList.get(currentTimer)

            val minuti = array.minuti.toString()
            min = minuti.toInt()

            val secondi = array.secondi.toString()
            sec = secondi.toInt()

            sum = (min * 60 * 1000) + (sec * 1000)

            playTimers(min, sec)
            currentTimer += 1

            val timer = java.util.Timer()

            val timerTask = object  : TimerTask(){
                override fun run() {
                    if(size > 1){
                        size -= 1
                        playAlarm()
                        readTimers()
                    }else{
                        size -= 1
                        playAlarmFinish()
                        readTimers()
                    }
                }
            }
            timer.schedule(timerTask, sum.toLong())
        }else{
            returnAllenamento()
        }
    }

    private fun playTimers(min: Int, sec: Int) {
        val i = Intent(this.requireContext(), CountdownActivity::class.java)
        i.putExtra("MINUTI", min)
        i.putExtra("SECONDI", sec)
        startActivity(i)
    }

    private fun returnAllenamento() {
        val i = Intent(this.requireContext(), TimerActivity2::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    private fun playAlarm(){
        val intent = Intent(this.requireContext(), BackgroundMusicService::class.java)
        activity?.startService(intent)
    }

    private fun playAlarmFinish(){
        val intent = Intent(this.requireContext(), BackgroundAlarmFinishService::class.java)
        activity?.startService(intent)
    }

    private fun changePath(){
        number_path += 1
        setrecyclerview()
    }
}
