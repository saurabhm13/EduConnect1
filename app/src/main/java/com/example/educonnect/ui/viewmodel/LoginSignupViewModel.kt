package com.example.educonnect.ui.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.educonnect.MyApplication
import com.example.educonnect.data.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit

class LoginSignupViewModel(): ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private var verificationId: String? = null

    private val _verificationCodeSent = MutableLiveData<Boolean>()
    val verificationCodeSent: LiveData<Boolean> get() = _verificationCodeSent

    private val _verificationError = MutableLiveData<String>()
    val verificationError: LiveData<String> get() = _verificationError

    var authCallback: (() -> Unit)? = null
    var errorCallback: (() -> Unit)? = null

    // Register user by email and password and also save user data in realtime database
    fun registerUser(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        val user = User(userId, username, email)
                        database.child("users").child(userId).setValue(user)
                        addSenderToken()
                    }
                    authCallback?.invoke()
                } else {
                    _verificationError.value = "Can't Register User! Try Again"
                }
            }
    }

    // Adding sender token in realtime database
    private fun addSenderToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Single Chat", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val senderId = FirebaseAuth.getInstance().currentUser?.uid

            if (senderId != null) {
                database.child("users").child(senderId).child("token").setValue(task.result)
            }

        })
    }

    // User Login using email and password
    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addSenderToken()
                    authCallback?.invoke()
                } else {
                    errorCallback?.invoke()
                }
            }
    }

    // Saving user data to realtime database
    fun saveUserToDatabase(name: String, email: String) {
        val userId = auth.currentUser?.uid
        userId?.let {
            val user = User(userId, name, email)
            database.child("users").child(userId).setValue(user).addOnCompleteListener {
                addSenderToken()
                authCallback?.invoke()
            }
        }
    }

    // Phone Number registration
    fun sendVerificationCode(phoneNo: String, countryCode: String, activity: Activity) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("$countryCode$phoneNo")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallBack)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        _verificationCodeSent.value = true
    }

    fun verifyCode(otp: String) {
        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it, otp) }

        if (credential != null) {
            signInWithCredential(credential)
        }
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode

                if (code != null) {
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _verificationError.value = e.message
            }
        }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authCallback?.invoke()
                } else {
                    _verificationError.value = task.exception?.message
                }
            }
    }

}