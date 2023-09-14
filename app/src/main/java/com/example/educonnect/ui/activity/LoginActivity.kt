package com.example.educonnect.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.educonnect.databinding.ActivityLoginBinding
import com.example.educonnect.ui.viewmodel.LoginSignupViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginSignupViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (auth.currentUser != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
            finish();
        }

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

                viewModel.loginUser(email!!, password!!)
                viewModel.authCallback = {
                    val intoMain = Intent(this, MainActivity::class.java)
                    startActivity(intoMain)
                    finish()
                }
                Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
            }
        }

    }
}