package com.example.educonnect.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.educonnect.R
import com.example.educonnect.data.UserChats
import com.example.educonnect.databinding.UserChatItemListBinding
import com.example.educonnect.util.Constants.Companion.REACHED
import com.example.educonnect.util.Constants.Companion.SEEN
import com.example.educonnect.util.Constants.Companion.SEND

class UserChatAdapter(
    private val onItemClick: (UserChats) -> Unit
): RecyclerView.Adapter<UserChatAdapter.UserChatViewHolder>() {

    var userChatsList = ArrayList<UserChats>()

    @SuppressLint("NotifyDataSetChanged")
    fun setUserChats(list: List<UserChats>) {
        userChatsList.clear()
        userChatsList = list as ArrayList<UserChats>
        notifyDataSetChanged()
    }

    class UserChatViewHolder(val binding: UserChatItemListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatViewHolder {
        return UserChatViewHolder(UserChatItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return userChatsList.size
    }

    override fun onBindViewHolder(holder: UserChatViewHolder, position: Int) {

        if (!userChatsList[position].image.isNullOrEmpty() && !userChatsList[position].image.equals("null")) {
            Glide.with(holder.itemView)
                .load(userChatsList[position].image)
                .into(holder.binding.profileImage)
        }else {
            Glide.with(holder.itemView)
                .load(R.drawable.profile_place_holder)
                .into(holder.binding.profileImage)
        }

        holder.binding.name.text = userChatsList[position].name
        holder.binding.date.text = userChatsList[position].timeStamp
        holder.binding.message.text = userChatsList[position].lastMessage

        if (userChatsList[position].status == SEND) {
            holder.binding.readStatus.setImageResource(R.drawable.ic_single_done)
            holder.binding.readStatus.setColorFilter(R.color.gray)
        }else if (userChatsList[position].status == REACHED) {
            holder.binding.readStatus.setImageResource(R.drawable.ic_double_check)
            holder.binding.readStatus.setColorFilter(R.color.gray)
        }else if (userChatsList[position].status == SEEN) {
            holder.binding.readStatus.setImageResource(R.drawable.ic_double_check)
            holder.binding.readStatus.setColorFilter(R.color.blue)
        }

        holder.binding.root.setOnClickListener {
            onItemClick.invoke(userChatsList[position])
        }
    }


}