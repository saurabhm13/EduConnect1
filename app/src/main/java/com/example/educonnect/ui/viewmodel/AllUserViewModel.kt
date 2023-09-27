package com.example.educonnect.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.educonnect.data.UserChats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllUserViewModel(): ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    lateinit var databaseReference: DatabaseReference

    private var allUserLiveData = MutableLiveData<List<UserChats>>()

    private val currentUser = auth.currentUser?.uid
    val userChatList = mutableListOf<UserChats>()

    private val _errorLiveData = MutableLiveData<String>()
    val observeError: LiveData<String> get() = _errorLiveData

    init {
        getChattingUsers()
        getAllUser()
    }

    private fun getAllUser() {
        databaseReference = database.getReference("users")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userList = mutableListOf<UserChats>()
                for (dataSnapshot in snapshot.children) {

                    if (dataSnapshot.key != currentUser && !userChatList.any {it.userId == dataSnapshot.key}) {
                        val user = dataSnapshot.getValue(UserChats::class.java)
                        user?.let {
                            userList.add(user)
                        }
                    }

                }

                allUserLiveData.value = userList
            }

            override fun onCancelled(error: DatabaseError) {
                _errorLiveData.value = error.toString()
            }

        })
    }

    private fun getChattingUsers() {
        databaseReference = database.getReference("users")

        if (currentUser != null) {
            database.getReference("users").child(currentUser).child("chats")
                .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    for (dataSnapshot in snapshot.children) {
                        val user = dataSnapshot.getValue(UserChats::class.java)
                        user?.let {
                            userChatList.add(user)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    _errorLiveData.value = error.toString()
                }

            })
        }
    }

    fun observeAllUserLiveData(): LiveData<List<UserChats>> {
        return allUserLiveData
    }

}