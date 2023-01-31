package it.pdm.timers.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import com.google.firebase.ktx.Firebase
import it.pdm.timers.*
import it.pdm.timers.R
import it.pdm.timers.Timer
import java.util.*
import kotlin.collections.ArrayList

/**
 * Classe che gestisce i Timer dell'applicazione
 */
class TimerFragment : Fragment() {
    private var min = 0
    private var sec = 0
    private var sum = 0
    private var currentTimer = 0
    private var size = 0
    var number_path = 0
    private lateinit var tv_recyclerview_number : TextView

    private val timerSalvatiFragment = TimerSalvatiFragment()

    private val open : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim) }
    private val close : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim) }

    private var clicked = false

    //recycler view timer
    private lateinit var rv_timer: RecyclerView
    private lateinit var TimeArrayList: ArrayList<Timer>
    private lateinit var TimerAdapter: Adapter

    private lateinit var communicator: Communicator
    private lateinit var number_allenamento : TextView

    var databaseReference: DatabaseReference? = null
    var databaseReference2: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    var eventListener2: ValueEventListener? = null
    var query: Query? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_timer, container, false)
        rv_timer = view.findViewById(R.id.recycler_view)
        number_allenamento = view.findViewById(R.id.tv_number_allenamento)
        communicator = activity as Communicator

        setrecyclerview()

        return view
    }

    /**
     * Metodo che permette di mostrare in alto a destra il menu_move_path
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.menu_move_path, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Metodo che gestisce gli eventi del menu_move_path
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.back -> backPath()
            R.id.next -> nextPath()
        }

        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openFunction(view)
    }

    /**
     * Metodo che gestisce i listener di ogni Floating Action Button
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun openFunction(view: View) {
        val fab = view?.findViewById<FloatingActionButton>(R.id.fab)
        val fabAdd = view?.findViewById<FloatingActionButton>(R.id.fab_add)
        val fabPlay = view?.findViewById<FloatingActionButton>(R.id.fab_play)
        val fabSave = view?.findViewById<FloatingActionButton>(R.id.fab_save)

        val color = Color.parseColor("#E2DFD2")
        fab.setColorFilter(color)
        fabAdd.setColorFilter(color)
        fabPlay.setColorFilter(color)
        fabSave.setColorFilter(color)


        fab.setOnClickListener {
            onAddButtonClicked()
        }

        fabAdd.setOnClickListener {
            passValue()
        }

        fabPlay.setOnClickListener {
            readTimers()
        }

        fabSave.setOnClickListener {
            saveData()
            nextPath()
        }
    }

    private fun onAddButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    /**
     * Metodo che permette di gestire le animazioni dei Floating Action Button
     */
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

    /**
     * Metodo che permette di impostare i Floating Action Button da INVISIBLE a VISIBLE
     */
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

    /**
     * Metodo che permette di passare il valore di number_path alla classe AddTimerFragment
     */
    private fun passValue(){
        communicator.passData(number_path)
    }

    /**
     * Metodo che permette di aprire il fragment passato come argomento
     */
    private fun openFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }

    /**
     * Metodo che permette di gestire la predisposizione della recycler_view della classe
     */
    fun setrecyclerview(){
        databaseReference = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Timers").child(Firebase.auth.currentUser!!.uid)

        query = databaseReference?.orderByChild("Allenamento " + number_path.toString())?.limitToLast(1)

        val gridLayoutManager = GridLayoutManager(this.requireContext(), 1)
        rv_timer.layoutManager = gridLayoutManager

        number_allenamento.text = number_path.toString()

        val builder = AlertDialog.Builder(this.requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_loading)
        val dialog = builder.create()
        dialog.show()

        TimeArrayList = ArrayList()
        TimerAdapter = Adapter(this.requireActivity(), TimeArrayList)
        rv_timer.adapter = TimerAdapter

        eventListener = query!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val np = "Allenamento " + number_path

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

    /**
     * Metodo che permette di salvare l'ArrayList della classe
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun saveData(){
        if (TimeArrayList.isEmpty()){
            Toast.makeText(this.requireContext(), "Non ci sono timer", Toast.LENGTH_SHORT).show()
            number_path -= 1
        }else {
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setCancelable(false)
            builder.setView(R.layout.progress_bar_loading)
            val dialog = builder.create()
            dialog.show()
            uploadData()
            dialog.dismiss()
        }
    }

    /**
     * Metodo che salva l'intera ArrayList nell'apposito path di Firebase
     */
    private fun uploadData(){
        val dataClass = Allenamenti(number_path.toString())

        FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Allenamenti").child(Firebase.auth.currentUser!!.uid)
            .child("Allenamento " + number_path)
            .setValue(dataClass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val inflater = LayoutInflater.from(this.requireContext())
                    val v = inflater.inflate(R.layout.allenamento_item, null)

                    tv_recyclerview_number = v.findViewById(R.id.tv_recyclerview_numbers)
                    tv_recyclerview_number.text = number_path.toString()
                    Toast.makeText(this.requireContext(), "salvato", Toast.LENGTH_SHORT).show()
                    openFragment(timerSalvatiFragment)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this.requireContext(), "errore", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Metodo che permette di leggere ogni Timer contenuto nell'ArrayList
     */
    private fun readTimers() {
        if (TimeArrayList.isEmpty()){
            Toast.makeText(this.requireContext(), "Non ci sono timer", Toast.LENGTH_SHORT).show()
        }else{
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
    }

    /**
     * Metodo che permette di aprire la classe CountdownActivity
     */
    private fun playTimers(min: Int, sec: Int) {
        val i = Intent(this.requireContext(), CountdownActivity::class.java)
        i.putExtra("MINUTI", min)
        i.putExtra("SECONDI", sec)
        startActivity(i)
    }

    /**
     * Metodo che permette di ritornare all'activity di partenza
     */
    private fun returnAllenamento() {
        val i = Intent(this.requireContext(), TimerActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    /**
     * Metodo che apre la classe BackgroundMusicService
     */
    private fun playAlarm(){
        val intent = Intent(this.requireContext(), BackgroundMusicService::class.java)
        activity?.startService(intent)
    }

    /**
     * Metodo che apre la classe BackgroundAlarmFinishService
     */
    private fun playAlarmFinish(){
        val intent = Intent(this.requireContext(), BackgroundAlarmFinishService::class.java)
        activity?.startService(intent)
    }

    /**
     * Metodo che permette di decrementare il valore del number_path
     */
    private fun backPath(){
        if (number_path > 0){
            number_path -= 1
            setrecyclerview()
        }else{
            Toast.makeText(this.requireContext(), "Non ci sono timer precedenti", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Metodo che permette di incrementare il valore del number_path
     */
    private fun nextPath(){
        number_path += 1
        setrecyclerview()
    }
}
