package com.example.educonnect.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.educonnect.R
import com.example.educonnect.data.User
import com.example.educonnect.databinding.ActivityUserDetailBinding
import com.example.educonnect.ui.viewmodel.LoginSignupViewModel
import com.example.educonnect.util.Constants.Companion.EMAIL
import com.example.educonnect.util.Constants.Companion.NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserDetailBinding

    private val viewModel: LoginSignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE

            val name = binding.name.editText?.text.toString().trim()
            val email = binding.email.editText?.text.toString().trim()

            viewModel.saveUserToDatabase(name, email)
            viewModel.authCallback = {
                val intoMain = Intent(this, MainActivity::class.java)
                startActivity(intoMain)
                finish()
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}