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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import it.pdm.timers.PasswordDimenticataActivity
import it.pdm.timers.R
import it.pdm.timers.User
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var database : DatabaseReference
    private var auth = Firebase.auth

    private lateinit var nas : TextView
    private lateinit var username : TextView
    private lateinit var mail : TextView
    private lateinit var password : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        nas = view.findViewById(R.id.tv_name_and_surname)
        username = view.findViewById(R.id.tv_username)
        mail = view.findViewById(R.id.tv_email)
        password = view.findViewById(R.id.tv_password)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tv_forgot_password = view.findViewById<TextView>(R.id.tv_modifica_password)
        insertData()
        tv_forgot_password.setOnClickListener {
            auth.signOut()
            val intent = Intent(this.requireContext(), PasswordDimenticataActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            Toast.makeText(this.requireContext(), "Sign Out", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertData(){
        val currentUser = auth.currentUser
        val uid = currentUser!!.uid
        database = FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Utenti")
            .child(uid)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)!!
                    nas.text = user.NameAndSurname
                    mail.text = user.email
                    username.text = user.username
                    password.text = user.password
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TEST","Failed to read value")
            }
        })
    }
/*
    private fun readData(username : String){
        database = FirebaseDatabase.getInstance().getReference("Utenti")
        database.child(username).get().addOnSuccessListener {
            if(it.exists()){
                val nas = it.child("nameAndSurname").value
                val userName = it.child("username").value
                val mail = it.child("email").value
                val password = it.child("password").value

                tv_nas.text = nas.toString()
                tv_username.text = userName.toString()
                tv_mail.text = mail.toString()
                tv_password.text = password.toString()
            }else{
                Toast.makeText(this.requireContext(), "errore", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(this.requireContext(), "errore", Toast.LENGTH_SHORT).show()
        }
    }*/
}