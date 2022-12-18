package it.pdm.timers

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val context: Activity, private val arrayList: ArrayList<Timer>):  RecyclerView.Adapter<Adapter.TimerViewHolder>() {
/*
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
       val inflater : LayoutInflater = LayoutInflater.from(context)
       val view : View = inflater.inflate(R.layout.list_item, null)

        val tvmin2 : TextView = view.findViewById(R.id.tv_min_2)
        val tvmin1 : TextView = view.findViewById(R.id.tv_min_1)

        tvmin2.text = arrayList[position].minuti
        tvmin1.text = arrayList[position].secondi

        return view
    }*/

    inner class TimerViewHolder(val v:View):RecyclerView.ViewHolder(v){
        val tvmin2 : TextView = v.findViewById(R.id.tv_min_2)
        val tvmin1 : TextView = v.findViewById(R.id.tv_min_1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view : View = inflater.inflate(R.layout.list_item,parent, false)
        return TimerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        val newList = arrayList[position]
        holder.tvmin2.text = newList.minuti
        holder.tvmin1.text = newList.secondi
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}