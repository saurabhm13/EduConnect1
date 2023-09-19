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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar

class SingleChatViewModel(): ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private var messageLiveData = MutableLiveData<List<Message>>()

    private val senderId = auth.currentUser?.uid

    private var senderImage: String? = null
    private lateinit var senderName: String

    init {
        getSenderData()
    }

    fun sendMessage(message: String, receiverId: String, receiverName: String, receiverImage: String) {

        val senderRoom = senderId+receiverId
        val receiverRoom = receiverId+senderId

        val sendMessageData = Message(message, getTime(), senderId)
        val senderSideMessageUpdate = UserChats(receiverId, receiverName, receiverImage, getDate(), message)
        val receiverSideMessageUpdate =
            senderId?.let { UserChats(it, senderName, senderImage, getDate(), message) }

        database.child("chats").child(senderRoom).push().setValue(sendMessageData)
        database.child("chats").child(receiverRoom).push().setValue(sendMessageData)

        if (senderId != null) {
            database.child("users").child(senderId).child("chats").child(receiverId).setValue(senderSideMessageUpdate)
            database.child("users").child(receiverId).child("chats").child(senderId).setValue(receiverSideMessageUpdate)
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

    private fun getSenderData() {
        if (senderId != null) {
            database.child("users").child(senderId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    senderImage = if (snapshot.hasChild("image")) {
                        snapshot.child("image").value as String
                    }else {
                        null
                    }

                    senderName = snapshot.child("name").value as String
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy")
        return formatter.format(time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm")
        return formatter.format(time)
    }

}