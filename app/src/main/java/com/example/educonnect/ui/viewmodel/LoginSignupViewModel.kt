package com.example.educonnect.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.educonnect.data.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit
import kotlin.math.log

class LoginSignupViewModel(): ViewModel() {

    val auth = FirebaseAuth.getInstance()
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
                    }
                    authCallback?.invoke()
                } else {

                }
            }
    }

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
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