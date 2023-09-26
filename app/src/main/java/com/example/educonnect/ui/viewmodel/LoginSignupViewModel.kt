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
    var errorCallback: (() -> Unit)? = null

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
                    errorCallback?.invoke()
                }
            }
    }

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

}