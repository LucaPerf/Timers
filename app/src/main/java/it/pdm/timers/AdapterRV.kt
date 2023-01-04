package it.pdm.timers

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AdapterRV(private val context: Activity, private val allenamentiList: ArrayList<Allenamenti>):
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recylerview_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.recCard.setOnClickListener {
            val intent = Intent(context, AllenamentoSalvatoActivity::class.java)
            intent.putExtra("Timers", allenamentiList[holder.adapterPosition].number_recyclerview)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allenamentiList.size
    }

}

class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var recCard : CardView
    var name_rv : TextView

    init {
        recCard = itemView.findViewById(R.id.recAllenamenti)
        name_rv = itemView.findViewById(R.id.tv_recyclerview_name)
    }
}