package it.pdm.timers

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AdapterRV(private val context: Activity, private val allenamentiList: ArrayList<Allenamenti>):
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.allenamento_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.number_rv.text = allenamentiList[position].number_recyclerview
        holder.recCard.setOnClickListener {
            val intent = Intent(context, AllenamentoSalvatoActivity::class.java)
            intent.putExtra("Timers", holder.number_rv.text.toString())
            context.startActivity(intent)
        }

        holder.tv_delete.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Elimina Allenamento")
            builder.setMessage("Sei sicuro di voler eliminare l'Allenamento?")
            builder.setIcon(R.drawable.ic_delete)
            builder.setCancelable(false)

            builder.setPositiveButton("Si"){_,_ ->
                val databaseReferenceAllenamenti = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("Allenamenti").child(Firebase.auth.currentUser!!.uid)
                databaseReferenceAllenamenti!!.child("Allenamento " + position).removeValue()
                Log.e("delete", "Allenamento " + position)

                val databaseReferenceTimers = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("Timers").child(Firebase.auth.currentUser!!.uid)
                databaseReferenceTimers.child("Allenamento " + position).removeValue()

                Toast.makeText(this.context, "Cancellato", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("No"){_,_ ->

            }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return allenamentiList.size
    }
}

class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var recCard : CardView
    var number_rv : TextView
    var tv_delete : TextView

    init {
        recCard = itemView.findViewById(R.id.recAllenamenti)
        number_rv = itemView.findViewById(R.id.tv_recyclerview_numbers)
        tv_delete = itemView.findViewById(R.id.tv_delete)
    }
}