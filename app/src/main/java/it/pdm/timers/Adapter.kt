package it.pdm.timers

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class Adapter(private val context: Activity, private val arrayList: ArrayList<Timer>): ArrayAdapter<Timer>(context,
    R.layout.list_item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
       val inflater : LayoutInflater = LayoutInflater.from(context)
       val view : View = inflater.inflate(R.layout.list_item, null)

        val NTimer : TextView = view.findViewById(R.id.tv_number_timer)
        val timeTimer : TextView = view.findViewById(R.id.tv_time)

        NTimer.text = arrayList[position].NTimer
        timeTimer.text = arrayList[position].timeTimer

        return view
    }
}