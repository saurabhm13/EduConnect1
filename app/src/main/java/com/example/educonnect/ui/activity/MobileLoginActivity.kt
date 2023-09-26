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

    private var verificationId: String? = null

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
//        setUpCallback()

        binding.txtHaveAnAccountMobileLogin.setOnClickListener {
            val intoLogin = Intent(this, LoginActivity::class.java)
            startActivity(intoLogin)
        }

        binding.btnPhoneLogin.setOnClickListener {

            countryCode = binding.countryCode.selectedCountryCode.toString()
            phoneNo = binding.phoneNo.editText?.text.toString().trim()
            otp = binding.otp.editText?.text.toString().trim()

            if (!phoneNo.isNullOrEmpty()) {
                if (otpSend) {
                    verifyCode(otp!!)
                }else {
                    otpSend = true
                    sendVerificationCode("+$countryCode$phoneNo")
                    binding.otp.visibility = View.VISIBLE
                    binding.btnPhoneLogin.text = "SignUp"
                }

            }
        }

    }

    private fun sendVerificationCode(phoneNo: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNo)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBack)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(otp: String) {
        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it, otp) }

        if (credential != null) {
            signInWithCredential(credential)
        }
    }

    // callback method is called on Phone auth provider.
    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode

                if (code != null) {
                    binding.otp.editText?.setText(code)

                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@MobileLoginActivity, e.message, Toast.LENGTH_LONG).show()
                Log.d("Mobile Login", e.message.toString())
            }
        }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {

                    val intoUserDetail = Intent(this, UserDetailActivity::class.java)
                    startActivity(intoUserDetail)
                    finish()

                } else {
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                }
            })
    }

}