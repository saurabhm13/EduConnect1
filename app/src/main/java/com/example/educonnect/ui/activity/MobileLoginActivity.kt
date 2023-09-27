package com.example.educonnect.ui.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.educonnect.R
import com.example.educonnect.databinding.ActivityMobileLoginBinding
import com.example.educonnect.ui.viewmodel.LoginSignupViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit


class MobileLoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityMobileLoginBinding

    private lateinit var auth: FirebaseAuth

    private val viewModel: LoginSignupViewModel by viewModels()

    private var countryCode: String? = null
    private var phoneNo: String? = null
    private var otp: String? = null
    private var otpSend = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.txtLoginMobleLogin.setOnClickListener {
            val intoLogin = Intent(this, LoginActivity::class.java)
            startActivity(intoLogin)
        }

        binding.btnPhoneLogin.setOnClickListener {

            countryCode = binding.countryCode.selectedCountryCode.toString()
            phoneNo = binding.phoneNo.editText?.text.toString().trim()
            otp = binding.otp.editText?.text.toString().trim()

            if (!phoneNo.isNullOrEmpty()) {
                if (otpSend) {
                    viewModel.verifyCode(otp!!)
                }else {
                    otpSend = true
                    viewModel.sendVerificationCode(phoneNo!!, "+$countryCode", this)
                    binding.otp.visibility = View.VISIBLE
                    binding.btnPhoneLogin.text = "SignUp"
                }

            }
        }

        viewModel.verificationError.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        viewModel.authCallback = {
            val intoUserDetail = Intent(this, UserDetailActivity::class.java)
            startActivity(intoUserDetail)
            finish()
        }

    }

}