package it.pdm.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Classe che gestisce il Login di un utente
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tv_register = findViewById<TextView>(R.id.tv_gia_registrato)

        tv_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val tv_forgot_password = findViewById<TextView>(R.id.tv_password_dimenticata)

        tv_forgot_password.setOnClickListener {
            val intent = Intent(this, PasswordDimenticataActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        val mail = findViewById<TextInputEditText>(R.id.et_email)
        val password = findViewById<TextInputEditText>(R.id.et_password)
        val btnLogin = findViewById<TextView>(R.id.btn_login)

        btnLogin.setOnClickListener {
            val mailString = mail.text.toString()
            val passwordString = password.text.toString()

            if(mailString.isNotEmpty() && passwordString.isNotEmpty()){
                auth.signInWithEmailAndPassword(mailString, passwordString).addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent(this, TimerActivity2::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Email o Password errati", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Alcuni campi sono vuoti", Toast.LENGTH_SHORT).show()
            }
        }
    }
}