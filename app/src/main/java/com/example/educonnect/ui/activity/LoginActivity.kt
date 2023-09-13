package com.example.educonnect.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.educonnect.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    var email: String? = null
    var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtCreateAccountLogin.setOnClickListener {
            val intoSignup = Intent(this, SignupActivity::class.java)
            startActivity(intoSignup)
        }

        binding.googleLoginCv.setOnClickListener {

        }

        binding.phoneLoginCv.setOnClickListener {
            val intoMobileLogin = Intent(this, MobileLoginActivity::class.java)
            startActivity(intoMobileLogin)
        }

        binding.btnLogin.setOnClickListener {
            email = binding.emailLogin.editText?.text.toString()
            password = binding.passwordLogin.editText?.text.toString()

            if (!email.isNullOrEmpty() || !password.isNullOrEmpty()) {
                Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
            }
        }

    }
}