package it.pdm.timers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.pdm.timers.R
import kotlinx.android.synthetic.main.fragment_add_timer.*

/**
 * A simple [Fragment] subclass.
 * Use the [add_timer.newInstance] factory method to
 * create an instance of this fragment.
 */
class add_timer : Fragment() {
    var sec1 = ""
    var sec2 = ""
    var min1 = ""
    var min2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        button0.setOnClickListener{
            setNum("0")
        }
        button1.setOnClickListener{
            setNum("1")
        }
        button2.setOnClickListener{
            setNum("2")
        }
        button3.setOnClickListener{
            setNum("3")
        }
        button4.setOnClickListener{
            setNum("4")
        }
        button5.setOnClickListener{
            setNum("5")
        }
        button6.setOnClickListener{
            setNum("6")
        }
        button7.setOnClickListener{
            setNum("7")
        }
        button8.setOnClickListener{
            setNum("8")
        }
        button9.setOnClickListener{
            setNum("9")
        }

        buttonBack.setOnClickListener {
            delete()
        }

        buttonCanc.setOnClickListener{
            deleteAll()
        }

        btn_save.setOnClickListener {
            createLV()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_timer, container, false)
    }

    private fun createLV(){

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
                        tv_Minutes2.text = "$min2"
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
}