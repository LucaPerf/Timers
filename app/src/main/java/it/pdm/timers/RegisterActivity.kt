package it.pdm.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tv_Login = findViewById<TextView>(R.id.tv_gia_account)

        tv_Login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val nameandsurname = findViewById<TextInputEditText>(R.id.et_name)
        val mail = findViewById<TextInputEditText>(R.id.et_email)
        val username = findViewById<TextInputEditText>(R.id.et_username)
        val password = findViewById<TextInputEditText>(R.id.et_password)
        val btn_Register = findViewById<Button>(R.id.btn_register)

        btn_Register.setOnClickListener {

            val uasString = nameandsurname.text.toString()
            val mailString = mail.text.toString()
            val usernameString = username.text.toString()
            val passwordString = password.text.toString()

            //creazione del path che vedremo nel firbase
           database = FirebaseDatabase.getInstance().getReference("Utenti")
            val utenti = User(uasString, mailString, usernameString, passwordString)
            //ogni username indicherà la persona all'interno del path nel firebase
            database.child(usernameString).setValue(utenti).addOnSuccessListener {
                nameandsurname.text?.clear()
                mail.text?.clear()
                username.text?.clear()
                password.text?.clear()

                Toast.makeText(this, "salvato con successo", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }.addOnFailureListener {
                Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show()
            }


        }
    }
}