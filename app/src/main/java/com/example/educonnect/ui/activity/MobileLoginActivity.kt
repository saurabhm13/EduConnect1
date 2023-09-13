package com.example.educonnect.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.educonnect.databinding.ActivityMobileLoginBinding

class MobileLoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityMobileLoginBinding

    var mobileNo: String? = null
    var otp: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtHaveAnAccountMobileLogin.setOnClickListener {
            val intoLogin = Intent(this, LoginActivity::class.java)
            startActivity(intoLogin)
        }

        binding.btnMobileLogin.setOnClickListener {
            if (!mobileNo.isNullOrEmpty()) {

            }
        }

    }
}