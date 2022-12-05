package it.pdm.timers.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import it.pdm.timers.Adapter
import it.pdm.timers.R
import it.pdm.timers.Timer
import kotlinx.android.synthetic.main.fragment_add_timer.*
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.android.synthetic.main.list_item.*

/**
 * A simple [Fragment] subclass.
 * Use the [add_timer.newInstance] factory method to
 * create an instance of this fragment.
 */
class add_timer : Fragment() {
    private lateinit var btn0: Button
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button
    private lateinit var btnCanc: Button
    private lateinit var btnBack: Button
    private lateinit var btnSave: Button

    lateinit var userArrayList : ArrayList<Timer>

    val timerFragment = TimerFragment()
    var sec1 = ""
    var sec2 = ""
    var min1 = ""
    var min2 = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_timer, container, false)
        btn0 = view.findViewById<Button>(R.id.button0)
        btn1 = view.findViewById<Button>(R.id.button1)
        btn2 = view.findViewById<Button>(R.id.button2)
        btn3 = view.findViewById<Button>(R.id.button3)
        btn4 = view.findViewById<Button>(R.id.button4)
        btn5 = view.findViewById<Button>(R.id.button5)
        btn6 = view.findViewById<Button>(R.id.button6)
        btn7 = view.findViewById<Button>(R.id.button7)
        btn8 = view.findViewById<Button>(R.id.button8)
        btn9 = view.findViewById<Button>(R.id.button9)
        btnBack = view.findViewById<Button>(R.id.buttonBack)
        btnCanc = view.findViewById<Button>(R.id.buttonCanc)
        btnSave = view.findViewById<Button>(R.id.btn_save)

        btn0.setOnClickListener {
            setNum("0")
        }

        btn1.setOnClickListener{
            setNum("1")
        }

        btn2.setOnClickListener{
            setNum("2")
        }
        btn3.setOnClickListener{
            setNum("3")
        }
        btn4.setOnClickListener{
            setNum("4")
        }
        btn5.setOnClickListener{
            setNum("5")
        }
        btn6.setOnClickListener{
            setNum("6")
        }
        btn7.setOnClickListener{
            setNum("7")
        }
        btn8.setOnClickListener{
            setNum("8")
        }
        btn9.setOnClickListener{
            setNum("9")
        }

        btnBack.setOnClickListener {
            delete()
        }

        btnCanc.setOnClickListener{
            deleteAll()
        }

        btnSave.setOnClickListener {
            createLV()
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun createLV(){
        saveData(
            tv_Minutes2.text.toString(), tv_Minutes1.text.toString(),
            tv_Seconds2.text.toString(), tv_Seconds1.text.toString())
        Log.d(TAG, "Timer aggiunto con successo")

        val lv = TimerFragment()
        createFragment(lv)
    }

    private fun delete(){
        val num = "0"
        if(!min2.isEmpty()){
            tv_Minutes2.text = num
            min2=""
        } else{
            if(!min1.isEmpty()){
                tv_Minutes1.text = num
                min1 = ""
            }else{
                if(!sec2.isEmpty()){
                    tv_Seconds2.text = num
                    sec2 = ""
                }else{
                    if(!sec1.isEmpty()){
                        tv_Seconds1.text = num
                        sec1=""
                    }
                }
            }
        }
    }

    private fun setNum(num: String){
        if(sec1.isEmpty()){
            sec1 += num
            tv_Seconds1.text = "$sec1"
        }else{
            if(sec2.isEmpty()){
                sec2 += num
                tv_Seconds2.text = "$sec2"
            } else{
                if(min1.isEmpty()){
                    min1 += num
                    tv_Minutes1.text = "$min1"
                } else{
                    if(min2.isEmpty()){
                        min2 += num
                        tv_Minutes2.text  = "$min2"
                    }
                }
            }
        }
    }

    private fun deleteAll(){
        val zero = "0"
        tv_Minutes2.text = zero
        tv_Minutes1.text = zero
        tv_Seconds2.text = zero
        tv_Seconds1.text = zero
        min2 = ""
        min1 = ""
        sec2 = ""
        sec1 = ""
    }

    private fun saveData(minuto2: String, minuto1: String,
                         secondo2: String, secondo1: String){
        val time = Timer("$minuto2", "$minuto1", "$secondo2", "$secondo1")
        //userArrayList.add(time)
        //timerFragment.lv_timer.adapter = Adapter(this.timerFragment.requireActivity(), timerFragment.TimeArrayList)
    }

    private fun createFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }

}