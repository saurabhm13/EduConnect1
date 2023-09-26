package com.example.educonnect.ui.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.educonnect.data.User
import com.google.android.gms.tasks.OnCompleteListener
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
    private lateinit var verificationId: String

    var authCallback: (() -> Unit)? = null

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

                }
            }
    }

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

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addSenderToken()
                    authCallback?.invoke()
                } else {
                    // Handle login failure
                }
            }
    }

    fun sendOTP(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks, context: Activity) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(context)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOTP(otp: String, onVerificationComplete: (PhoneAuthCredential) -> Unit) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        onVerificationComplete(credential)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authCallback?.invoke()
                } else {
                    // Failed to sign in, handle the error.
                }
            }
    }

//    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            // OTP is automatically verified
//            // Proceed with sign-in using credential
//            auth.signInWithCredential(credential)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        authCallback?.invoke()
//                    } else {
//                        // Handle login failure
//                    }
//                }
//        }
//
//        override fun onVerificationFailed(e: FirebaseException) {
//            // Handle verification failure
//        }
//
////        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
////            // Save the verificationId for later use
////            viewModel.verificationId = verificationId
////        }
//    }

}