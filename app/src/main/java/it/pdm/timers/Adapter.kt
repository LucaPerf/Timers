package it.pdm.timers

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val context: Activity, private val arrayList: ArrayList<Timer>):
    RecyclerView.Adapter<TimerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.timer_item, parent, false)
        return TimerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.minuti.text = arrayList[position].minuti
        holder.secondi.text = arrayList[position].secondi
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}

class TimerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var minuti: TextView
    var secondi: TextView

    init {
        minuti = itemView.findViewById(R.id.tv_min)
        secondi = itemView.findViewById(R.id.tv_sec)
    }
}