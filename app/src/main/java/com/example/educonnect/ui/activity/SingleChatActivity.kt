package com.example.educonnect.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.educonnect.R
import com.example.educonnect.databinding.ActivitySingleChatBinding
import com.example.educonnect.util.Constants.Companion.IMAGE
import com.example.educonnect.util.Constants.Companion.NAME

class SingleChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIncomingData()

        binding.imgBack.setOnClickListener {
            finish()
        }

    }

    private fun getIncomingData() {
        binding.nameSingleChat.text = intent.getStringExtra(NAME)

        if (intent.getStringExtra(IMAGE) == null) {
            Glide.with(this)
                .load(R.drawable.profile_place_holder)
                .into(binding.profileImageSingleChat)
        }else {
            Glide.with(this)
                .load(intent.getStringExtra(IMAGE))
                .into(binding.profileImageSingleChat)
        }
    }
}