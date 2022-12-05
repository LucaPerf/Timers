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

        val tvmin2 : TextView = view.findViewById(R.id.tv_min_2)
        val tvmin1 : TextView = view.findViewById(R.id.tv_min_1)
        val tvsec2 : TextView = view.findViewById(R.id.tv_sec_2)
        val tvsec1 : TextView = view.findViewById(R.id.tv_sec_1)

        tvmin2.text = arrayList[position].minuto2
        tvmin1.text = arrayList[position].minuto1
        tvsec2.text = arrayList[position].secondo2
        tvsec1.text = arrayList[position].secondo1

        return view
    }
}