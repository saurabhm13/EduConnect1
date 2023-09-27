package com.example.educonnect.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.educonnect.databinding.ActivityAllUserBinding
import com.example.educonnect.ui.adapter.AllUserAdapter
import com.example.educonnect.ui.viewmodel.AllUserViewModel
import com.example.educonnect.util.Constants
import com.example.educonnect.util.Constants.Companion.ID
import com.example.educonnect.util.Constants.Companion.IMAGE
import com.example.educonnect.util.Constants.Companion.NAME

class AllUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityAllUserBinding

    private val viewModel: AllUserViewModel by viewModels()
    private lateinit var allUserAdapter: AllUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fetch and display all registered users
        prepareRecyclerView()
        viewModel.observeAllUserLiveData().observe(this) {
            allUserAdapter.setUserChats(it)
        }

        binding.back.setOnClickListener {
            finish()
        }

        // Observe Error
        viewModel.observeError.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

    }

    private fun prepareRecyclerView() {

        allUserAdapter = AllUserAdapter{
            val intoSingleChat = Intent(this, SingleChatActivity::class.java)
            intoSingleChat.putExtra(ID, it.userId)
            intoSingleChat.putExtra(NAME, it.name)
            intoSingleChat.putExtra(IMAGE, it.image)
            startActivity(intoSingleChat)
            finish()
        }

        binding.rvAllUser.apply {
            layoutManager = LinearLayoutManager(this@AllUserActivity, LinearLayoutManager.VERTICAL, false)
            adapter = allUserAdapter
        }
    }
}