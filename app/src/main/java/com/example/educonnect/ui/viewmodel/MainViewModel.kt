package com.example.educonnect.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.educonnect.data.User
import com.example.educonnect.data.UserChats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel(): ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    lateinit var databaseReference: DatabaseReference

    private var userChatLiveData = MutableLiveData<List<UserChats>>()
    private var userDataLiveData = MutableLiveData<User>()

    private val userId = auth.currentUser?.uid

    fun getUserChat() {
        databaseReference = userId?.let { database.getReference("users").child(it).child("chats") }!!

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userList = mutableListOf<UserChats>()
                for (dataSnapshot in snapshot.children) {

                    val user = dataSnapshot.getValue(UserChats::class.java)
                    user?.let {
                        userList.add(user)
                    }
                }

                userChatLiveData.value = userList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun observeUserChatsLiveData(): LiveData<List<UserChats>> {
        return userChatLiveData
    }

    fun getUserData() {
        if (userId != null) {
            database.reference.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val userData = snapshot.getValue(User::class.java)!!
                    userDataLiveData.value = userData
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    fun observeUserData(): LiveData<User> {
        return userDataLiveData
    }

}