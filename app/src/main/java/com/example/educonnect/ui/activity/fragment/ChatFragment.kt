package com.example.educonnect.ui.activity.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.educonnect.R
import com.example.educonnect.databinding.FragmentChatBinding
import com.example.educonnect.ui.activity.AllUserActivity
import com.example.educonnect.ui.activity.SingleChatActivity
import com.example.educonnect.ui.adapter.UserChatAdapter
import com.example.educonnect.ui.viewmodel.MainViewModel
import com.example.educonnect.util.Constants

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding

    private val viewModel: MainViewModel by viewModels()
    private lateinit var userChatAdapter: UserChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)

        binding.btnNew.setOnClickListener {
            val intoNewChat = Intent(activity, AllUserActivity::class.java)
            startActivity(intoNewChat)
        }

        prepareRecyclerView()
        // Observe changes in the user chats and update the adapter.
        viewModel.observeUserChatsLiveData().observe(viewLifecycleOwner) {
            userChatAdapter.setUserChats(it)
        }

        return binding.root
    }

    private fun prepareRecyclerView() {

        // Retrieve user chat data from ViewModel.
        viewModel.getUserChat()

        userChatAdapter = UserChatAdapter {
            val intoSingleChat = Intent(activity, SingleChatActivity::class.java)
            intoSingleChat.putExtra(Constants.ID, it.userId)
            intoSingleChat.putExtra(Constants.NAME, it.name)
            intoSingleChat.putExtra(Constants.IMAGE, it.image)
            startActivity(intoSingleChat)
        }

        binding.rvUserChat.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = userChatAdapter
        }

        // Observe Error
        viewModel.observeError.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        }
    }

}