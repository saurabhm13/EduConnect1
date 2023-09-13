package com.example.educonnect.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.educonnect.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    var name: String? = null
    var email: String? = null
    var password: String? = null
    var conformPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtHaveAnAccount.setOnClickListener {
            val intoLogin = Intent(this, LoginActivity::class.java)
            startActivity(intoLogin)
        }

        binding.btnSignup.setOnClickListener {
            name = binding.nameSignup.editText?.text.toString()
            email = binding.emailSignup.editText?.text.toString()
            password = binding.passwordSignup.editText?.text.toString()
            conformPassword = binding.conformPassSignup.editText?.text.toString()
        }


    }
}