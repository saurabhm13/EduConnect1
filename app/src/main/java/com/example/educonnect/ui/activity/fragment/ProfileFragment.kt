package com.example.educonnect.ui.activity.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.educonnect.R
import com.example.educonnect.data.User
import com.example.educonnect.databinding.FragmentProfileBinding
import com.example.educonnect.ui.activity.EditProfileActivity
import com.example.educonnect.ui.activity.LoginActivity
import com.example.educonnect.ui.activity.MainActivity
import com.example.educonnect.ui.viewmodel.MainViewModel
import com.example.educonnect.util.Constants.Companion.EMAIL
import com.example.educonnect.util.Constants.Companion.IMAGE
import com.example.educonnect.util.Constants.Companion.NAME
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private var viewModel = MainViewModel()

    private lateinit var name: String
    private lateinit var image: String
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

//        userData = viewModel.getUserData()!!
        viewModel.getUserData()
        viewModel.observeUserData().observe(viewLifecycleOwner) {
            name = it.name
            image = it.image.toString()
            email = it.email.toString()
            addFields()
        }

        binding.logout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            // After signing out, navigate the user to the login screen or perform other actions as needed.
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.profileLayout.setOnClickListener {
            val intoEditProfile = Intent(activity, EditProfileActivity::class.java)
            intoEditProfile.putExtra(NAME, name)
            intoEditProfile.putExtra(EMAIL, email)
            intoEditProfile.putExtra(IMAGE, image)
            startActivity(intoEditProfile)
        }

        return (binding.root)
    }

    private fun addFields() {
        binding.nameProfile.text = name

        if (image.isEmpty() || image == "null") {
            Glide.with(this)
                .load(R.drawable.profile_place_holder)
                .into(binding.profileImageProfile)
        }else {
            Glide.with(this)
                .load(image)
                .into(binding.profileImageProfile)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserData()
    }
}