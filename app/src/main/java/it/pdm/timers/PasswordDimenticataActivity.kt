package it.pdm.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Classe che gestisce il recupero password oppure il cambio password
 */
class PasswordDimenticataActivity : AppCompatActivity() {
    private lateinit var et_mail: EditText
    private lateinit var btnResetPassword: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_dimenticata)

        et_mail = findViewById(R.id.et_email)
        btnResetPassword = findViewById<Button>(R.id.btn_avanti)
        auth = Firebase.auth

        btnResetPassword.setOnClickListener {
            val sPassord = et_mail.text.toString()

            auth.sendPasswordResetEmail(sPassord)
                .addOnSuccessListener {
                    Toast.makeText(this, "Controlla la tua mail!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}