package com.example.educonnect.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.educonnect.databinding.ActivitySignupBinding
import com.example.educonnect.ui.viewmodel.LoginSignupViewModel

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    private val viewModel: LoginSignupViewModel by viewModels()

    var name: String? = null
    var email: String? = null
    var password: String? = null
    var conformPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtLogin.setOnClickListener {
            val intoLogin = Intent(this, LoginActivity::class.java)
            startActivity(intoLogin)
        }

        binding.btnSignup.setOnClickListener {
            name = binding.nameSignup.editText?.text.toString()
            email = binding.emailSignup.editText?.text.toString()
            password = binding.passwordSignup.editText?.text.toString()
            conformPassword = binding.conformPassSignup.editText?.text.toString()

            viewModel.registerUser(email!!, password!!, name!!)
            viewModel.authCallback = {
                val intoMain = Intent(this, MainActivity::class.java)
                startActivity(intoMain)
                finish()
            }
        }


    }
}