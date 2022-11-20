package it.pdm.timers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_set_time_activty.*

class SetTimeActivty : AppCompatActivity() {
    var sec1 = ""
    var min1 = ""
    var ore1 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_time_activty)

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
    }

    private fun setNum(num: String){
        if(sec1.isEmpty()){
            sec1 += num
            tv_Seconds.text = "$sec1"
        }else{
            if(min1.isEmpty()){
                min1 += num
                tv_Minutes.text = "$min1"
            }else{
                if(ore1.isEmpty()) {
                    ore1 += num
                    tv_Houres1.text = "$ore1"
                }
            }
        }

    }
}