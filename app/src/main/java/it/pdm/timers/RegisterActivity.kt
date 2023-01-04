package it.pdm.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

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

        val nameandsurname = findViewById<TextInputEditText>(R.id.et_name)
        val mail = findViewById<TextInputEditText>(R.id.et_email)
        val username = findViewById<TextInputEditText>(R.id.et_username)
        val password = findViewById<TextInputEditText>(R.id.et_password)
        val confirmpassword = findViewById<TextInputEditText>(R.id.et_confirm_password)
        val btn_Register = findViewById<Button>(R.id.btn_register)

        auth = Firebase.auth
        btn_Register.setOnClickListener {

            val uasString = nameandsurname.text.toString()
            val mailString = mail.text.toString()
            usernameString = username.text.toString()
            val passwordString = password.text.toString()
            val confirmPasswordString = confirmpassword.text.toString()
            var check = true

            if (uasString.isNotEmpty() && mailString.isNotEmpty() && usernameString.isNotEmpty() && passwordString.isNotEmpty()) {
                if(!isValidNameAndSurname(uasString)){
                    Toast.makeText(this, "inserire un nome e cognome di almeno 5 caratteri", Toast.LENGTH_SHORT).show()
                    check = false
                }

                if (!isValidEmail(mailString)){
                    Toast.makeText(this, "e-mail non valida", Toast.LENGTH_SHORT).show()
                    check = false
                }

                if(!isValidUsername(usernameString)){
                    Toast.makeText(this, "inserire username di almeno 4 caratteri", Toast.LENGTH_SHORT).show()
                    check = false
                }

                if(!isValidPassword(passwordString)){
                    Toast.makeText(this, "inserire una password di almeno 5 caratteri", Toast.LENGTH_SHORT).show()
                    check = false
                }

                if (passwordString != confirmPasswordString) {
                    Toast.makeText(this, "Password non sono uguali", Toast.LENGTH_SHORT).show()
                    check = false
                }
                if(check){

                    auth.createUserWithEmailAndPassword(mailString, passwordString)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Utente già registrato", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            //creazione del path che vedremo nel firbase
                            database =
                                FirebaseDatabase.getInstance("https://timers-46b2e-default-rtdb.europe-west1.firebasedatabase.app")
                                    .getReference("Utenti").child(Firebase.auth.currentUser!!.uid)
                            val utenti = User(uasString, mailString, usernameString, passwordString)
                            //ogni username indicherà la persona all'interno del path nel firebase
                            database.child(usernameString).setValue(utenti).addOnSuccessListener {
                                nameandsurname.text?.clear()
                                mail.text?.clear()
                                username.text?.clear()
                                password.text?.clear()

                                Toast.makeText(this, "salvato con successo", Toast.LENGTH_SHORT)
                                    .show()

                            }.addOnFailureListener {
                                Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Alcuni campi sono vuoti", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean{
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(||-[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isValidPassword(pass: String?): Boolean{
        return pass != null && pass.length >= 4
    }

    private fun isValidNameAndSurname(userandsurname: String?): Boolean{
        return userandsurname != null && userandsurname.length >= 4
    }

    private fun isValidUsername(username: String?): Boolean{
        return username != null && username.length >= 3
    }

}