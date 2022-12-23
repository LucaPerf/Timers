package it.pdm.timers

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterRV(private val context: Activity, private val allenamentiList: ArrayList<Allenamenti>): RecyclerView.Adapter<AdapterRV.MyViewHolder>() {

    private lateinit var mListener: onItemClickListner

    interface onItemClickListner{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListner(listener: onItemClickListner){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recylerview_item,
        parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = allenamentiList[position]
        holder.numbers.text = currentItem.numbers_recyclerview
    }

    override fun getItemCount(): Int {
        return allenamentiList.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListner): RecyclerView.ViewHolder(itemView){
        val numbers: TextView = itemView.findViewById(R.id.tv_recyclerview_numbers)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }
}