package com.example.educonnect.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.educonnect.R
import com.example.educonnect.databinding.ActivitySingleChatBinding
import com.example.educonnect.ui.adapter.MessageAdapter
import com.example.educonnect.ui.viewmodel.SingleChatViewModel
import com.example.educonnect.util.Constants.Companion.ID
import com.example.educonnect.util.Constants.Companion.IMAGE
import com.example.educonnect.util.Constants.Companion.NAME

class SingleChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleChatBinding

    private lateinit var receiverName: String
    private lateinit var receiverId: String
    private lateinit var receiverImage: String

    private val viewModel: SingleChatViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIncomingData()

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnSend.setOnClickListener {

            val message = binding.editMessage.text.toString().trim()

            if (message != "") {
                viewModel.sendMessage(message, receiverId, receiverName, receiverImage)
                binding.editMessage.setText("")
            }
        }

        viewModel.getMessage(receiverId)
        prepareRecyclerView()
        viewModel.observeMessageLiveData().observe(this) {
            messageAdapter.setMessage(it)
        }

    }

    private fun prepareRecyclerView() {
        messageAdapter = MessageAdapter()

        binding.rvMessage.apply {
            layoutManager = LinearLayoutManager(this@SingleChatActivity, LinearLayoutManager.VERTICAL, false)
            adapter = messageAdapter
        }
    }

    private fun getIncomingData() {

        receiverName = intent.getStringExtra(NAME).toString()
        receiverImage = intent.getStringExtra(IMAGE).toString()
        binding.nameSingleChat.text = receiverName

        if (intent.getStringExtra(IMAGE) == null || receiverImage == "null") {
            Glide.with(this)
                .load(R.drawable.profile_place_holder)
                .into(binding.profileImageSingleChat)
        }else {
            Glide.with(this)
                .load(intent.getStringExtra(IMAGE))
                .into(binding.profileImageSingleChat)
        }

        receiverId = intent.getStringExtra(ID).toString()
    }
}