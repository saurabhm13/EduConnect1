package com.example.educonnect.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.data.Message
import com.example.educonnect.databinding.ReceivedMessageItemListBinding
import com.example.educonnect.databinding.SendMessageItemListBinding
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val SENDER = 1
        private const val RECEIVER = 2
    }

    private val messageList = ArrayList<Message>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMessage(messageList: List<Message>) {
        this.messageList.clear()
        this.messageList.addAll(messageList)
        notifyDataSetChanged()
    }

    class SenderViewHolder(val binding: SendMessageItemListBinding): RecyclerView.ViewHolder(binding.root)

    class ReceiverViewHolder(val binding: ReceivedMessageItemListBinding): RecyclerView.ViewHolder(binding.root)


    override fun getItemViewType(position: Int): Int {
        val item = messageList[position]

        return if (item.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            SENDER
        } else {
            RECEIVER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SENDER -> {
                SenderViewHolder(
                    SendMessageItemListBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false))
            }
            RECEIVER -> {
                ReceiverViewHolder(
                    ReceivedMessageItemListBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = messageList[position]

        when (holder.itemViewType) {
            SENDER -> {
                val senderHolder = holder as SenderViewHolder
                senderHolder.binding.sendMessage.text = item.message
                senderHolder.binding.sendMessageTime.text = item.timeStamp
            }
            RECEIVER -> {
                val receiverHolder = holder as ReceiverViewHolder
                receiverHolder.binding.receivedMessage.text = item.message
                receiverHolder.binding.receivedMessageTime.text = item.timeStamp
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}