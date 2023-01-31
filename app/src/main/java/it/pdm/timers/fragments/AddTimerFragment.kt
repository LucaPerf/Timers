package it.pdm.timers.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import it.pdm.timers.R
import it.pdm.timers.Timer
import kotlinx.android.synthetic.main.fragment_add_timer.*
import java.text.DateFormat
import java.util.*

/**
 * Classe che permette di impostare i valori del timer
 */
class AddTimerFragment : Fragment() {
    var output: Int ?= 0

    /**
     * Metodo che permette di implementare i valori della number_picker
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_timer, container, false)

        output = arguments?.getInt("message")
        Log.e("path", output.toString())

        val np_min = view.findViewById<NumberPicker>(R.id.np_minutes)
        val np_sec = view.findViewById<NumberPicker>(R.id.np_seconds)

        np_min.minValue = 0
        np_min.maxValue = 60

        np_sec.minValue = 0
        np_sec.maxValue = 60

        np_min.setOnValueChangedListener { _, _, _ ->
            val minutess = np_min.value
            if("$minutess".equals("0") || "$minutess".equals("1") || "$minutess".equals("2") || "$minutess".equals("3") ||
                "$minutess".equals("4") || "$minutess".equals("5") || "$minutess".equals("6") || "$minutess".equals("7") ||
                "$minutess".equals("8") || "$minutess".equals("9")){
                result_minutes.text = String.format("0$minutess")
            }else{
                result_minutes.text = String.format("$minutess")
            }

        }

        np_sec.setOnValueChangedListener { _, _, _ ->
            val secondss = np_sec.value
            if("$secondss".equals("0") || "$secondss".equals("1") || "$secondss".equals("2") || "$secondss".equals("3") ||
                "$secondss".equals("4") || "$secondss".equals("5") || "$secondss".equals("6") || "$secondss".equals("7") ||
                "$secondss".equals("8") || "$secondss".equals("9")){
                result_seconds.text = String.format("0$secondss")
            }else{
                result_seconds.text = String.format("$secondss")
            }

        }

        var save_btn = view.findViewById<Button>(R.id.savebutton)

        save_btn.setOnClickListener {
            saveDataTimer()
        }

        // Inflate the layout for this fragment
        return view
    }

    /**
     * Metodo che permette di salvare i dati appena impostati
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun saveDataTimer(){
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_loading)
        val dialog = builder.create()
        dialog.show()
        uploadDataTimer()
        dialog.dismiss()
    }

    /**
     * Metodo che permette di aggiungere i dati appena scelti nell'apposito path di Firebase
     */
    private fun uploadDataTimer(){
        val dataClass = Timer(result_minutes?.text.toString(), result_seconds?.text.toString())
        val currentData = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        output?.let {
            FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Timers").child(Firebase.auth.currentUser!!.uid).child("Allenamento " + it.toString())
                .child(currentData)
                .setValue(dataClass).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this.requireContext(), "Aggiunto", Toast.LENGTH_SHORT).show()
                        val timerFragment = TimerFragment()
                        timerFragment.number_path = output!!.toInt()
                        createFragment(timerFragment)
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this.requireContext(), "Errore", Toast.LENGTH_SHORT).show()
                }
        }
    }

    /**
     * Metodo che permette di aprire il fragment indicato nell'argomento
     */
    private fun createFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }

}