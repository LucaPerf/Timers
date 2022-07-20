package it.pdm.timers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lateinit var  option : Spinner
        option = findViewById(R.id.sp_opinion)

        val options = arrayOf(1,2,3,4,5,6,7,8,9,10)

        option.adapter = ArrayAdapter<Int>(this,android.R.layout.simple_list_item_1, options)
    }
}