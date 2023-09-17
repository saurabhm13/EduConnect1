package com.example.educonnect.ui.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private val viewModel: LoginSignupViewModel by viewModels()

    private var verificationId: String? = null

//    private var countryCode: String? = null
    private var phoneNo: String? = null
    private var otp: String? = null
    private var otpSend = false

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

//            countryCode = binding.countryCode.textView_selectedCountry.toString()
            phoneNo = binding.phoneNo.editText?.text.toString().trim()
            otp = binding.otp.editText?.text.toString().trim()

            if (!phoneNo.isNullOrEmpty()) {
                if (otpSend) {
//                    viewModel.verifyOTP(otp!!) {
//                        viewModel.signInWithPhoneAuthCredential(it)
//                    }
                    verifyCode(otp!!)
                }else {
//                    viewModel.sendOTP("+91$phoneNo", getOtpCallbacks(), this)
                    otpSend = true
                    sendVerificationCode("+91$phoneNo")
                }

            }
        }

    }



    private fun getOtpCallbacks() = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Automatically handle verification on some devices
            viewModel.signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Handle verification failure
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
//            this@MobileLoginActivity.verificationId = verificationId
            Toast.makeText(applicationContext, "Send", Toast.LENGTH_SHORT).show()
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
        // inside this method we are checking if
        // the code entered is correct or not.
        auth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    // if the code is correct and the task is successful
                    // we are sending our user to new activity.
                    val i = Intent(this@MobileLoginActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    Toast.makeText(this@MobileLoginActivity, task.exception!!.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

//    private fun setUpCallback() {
//        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                // OTP is automatically verified
//                signInWithCredential(credential)
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                // Handle verification failure
//            }
//
//            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
//                // Save the verificationId for later use
//                viewModel.verificationId = verificationId
//            }
//        }
//    }
}