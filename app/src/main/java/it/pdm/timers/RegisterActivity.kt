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
            val usernameString = username.text.toString()
            val passwordString = password.text.toString()
            val confirmPasswordString = confirmpassword.text.toString()

            if(uasString.isNotEmpty() && mailString.isNotEmpty() && usernameString.isNotEmpty() && passwordString.isNotEmpty()){
                if(passwordString == confirmPasswordString){
                    auth.createUserWithEmailAndPassword(mailString, passwordString).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }else{
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