package com.example.educonnect.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.educonnect.data.Message
import com.example.educonnect.data.UserChats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar

class SingleChatViewModel(): ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private var messageLiveData = MutableLiveData<List<Message>>()

    private val senderId = auth.currentUser?.uid

    fun sendMessage(message: String, receiverId: String, receiverName: String, receiverImage: String) {

        val senderRoom = senderId+receiverId
        val receiverRoom = receiverId+senderId

        val sendMessageData = Message(message, getTime(), senderId)
        val latestMessageData = UserChats(receiverId, receiverName, receiverImage, getDate(), message)

        database.child("chats").child(senderRoom).push().setValue(sendMessageData)
        database.child("chats").child(receiverRoom).push().setValue(sendMessageData)

        if (senderId != null) {
            database.child("users").child(senderId).child("chats").child(receiverId).setValue(latestMessageData)
        }

    }

    fun getMessage(receiverId: String) {

        database.child("chats").child(senderId+receiverId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val messageList = mutableListOf<Message>()
                for (dataSnapshot in snapshot.children) {

                    val message = dataSnapshot.getValue(Message::class.java)
                    message?.let {
                        messageList.add(it)
                    }
                }

                messageLiveData.value = messageList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun observeMessageLiveData(): LiveData<List<Message>> {
        return messageLiveData
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm")
        return formatter.format(time)
    }

}