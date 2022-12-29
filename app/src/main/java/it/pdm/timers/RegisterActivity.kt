package it.pdm.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var usernameString: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tv_Login = findViewById<TextView>(R.id.tv_gia_account)

        tv_Login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        val nameandsurname = findViewById<TextInputEditText>(R.id.et_name)
        val mail = findViewById<TextInputEditText>(R.id.et_email)
        val username = findViewById<TextInputEditText>(R.id.et_username)
        val password = findViewById<TextInputEditText>(R.id.et_password)
        val confirmpassword = findViewById<TextInputEditText>(R.id.et_confirm_password)
        val btn_Register = findViewById<Button>(R.id.btn_register)

        btn_Register.setOnClickListener {

            val uasString = nameandsurname.text.toString()
            val mailString = mail.text.toString()
            usernameString = username.text.toString()
            val passwordString = password.text.toString()
            val confirmPasswordString = confirmpassword.text.toString()

            if(uasString.isNotEmpty() && mailString.isNotEmpty() && usernameString.isNotEmpty() && passwordString.isNotEmpty()){
                if(passwordString == confirmPasswordString){

                    auth.createUserWithEmailAndPassword(mailString, passwordString).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "Utente già registrato", Toast.LENGTH_SHORT).show()
                        }
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

                        }.addOnFailureListener {
                            Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Password non sono uguali", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Alcuni campi sono vuoti", Toast.LENGTH_SHORT).show()
            }



        }
    }
}