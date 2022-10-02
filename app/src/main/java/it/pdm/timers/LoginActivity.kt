package it.pdm.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
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


        val btnLogin = findViewById<TextView>(R.id.btn_login)

        btnLogin.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}