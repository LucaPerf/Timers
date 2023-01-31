package it.pdm.timers.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import it.pdm.timers.PasswordDimenticataActivity
import it.pdm.timers.R
import it.pdm.timers.User
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth

    private lateinit var nas : TextView
    private lateinit var username : TextView
    private lateinit var mail : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        nas = view.findViewById(R.id.tv_name_and_surname)
        username = view.findViewById(R.id.tv_username)
        mail = view.findViewById(R.id.tv_email)
        auth = Firebase.auth

        val tv_forgot_password = view.findViewById<TextView>(R.id.tv_modifica_password)
        tv_forgot_password.setOnClickListener {
            auth.signOut()
            val intent = Intent(this.requireContext(), PasswordDimenticataActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            Toast.makeText(this.requireContext(), "Sign Out", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertData()
    }

    private fun insertData(){
        database = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("Utenti")
            .child(Firebase.auth.currentUser!!.uid)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)!!
                    mail.text = user.email
                    nas.text = user.nameAndSurname
                    username.text = user.username
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TEST","Failed to read value")
            }
        })
    }
}