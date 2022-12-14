package it.pdm.timers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterRV(private val allenamentiList: ArrayList<Allenamenti>): RecyclerView.Adapter<AdapterRV.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recylerview_item,
        parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = allenamentiList[position]
        holder.numbers.text = currentItem.numbers_recyclerview
    }

    override fun getItemCount(): Int {
        return allenamentiList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val numbers: TextView = itemView.findViewById(R.id.tv_recyclerview_numbers)
    }
}