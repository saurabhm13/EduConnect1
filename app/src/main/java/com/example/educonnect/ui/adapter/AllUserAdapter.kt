package com.example.educonnect.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.educonnect.R
import com.example.educonnect.data.UserChats
import com.example.educonnect.databinding.AllUsersItemListBinding
import com.example.educonnect.databinding.UserChatItemListBinding
import com.example.educonnect.util.Constants.Companion.REACHED
import com.example.educonnect.util.Constants.Companion.SEEN
import com.example.educonnect.util.Constants.Companion.SEND

class AllUserAdapter(
    private val onItemClick: (UserChats) -> Unit
): RecyclerView.Adapter<AllUserAdapter.UserChatViewHolder>() {

    private var allUserList = ArrayList<UserChats>()

    @SuppressLint("NotifyDataSetChanged")
    fun setUserChats(list: List<UserChats>) {
        allUserList.clear()
        allUserList.addAll(list)
        notifyDataSetChanged()
    }

    class UserChatViewHolder(val binding: AllUsersItemListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatViewHolder {
        return UserChatViewHolder(AllUsersItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return allUserList.size
    }

    override fun onBindViewHolder(holder: UserChatViewHolder, position: Int) {

        if (!allUserList[position].image.isNullOrEmpty()) {
            Glide.with(holder.itemView)
                .load(allUserList[position].image)
                .into(holder.binding.profileImage)
        }else {
            Glide.with(holder.itemView)
                .load(R.drawable.profile_place_holder)
                .into(holder.binding.profileImage)
        }

        holder.binding.name.text = allUserList[position].name

        holder.binding.root.setOnClickListener {
            onItemClick.invoke(allUserList[position])
        }
    }


}