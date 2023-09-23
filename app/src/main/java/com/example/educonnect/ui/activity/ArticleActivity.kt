package com.example.educonnect.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.educonnect.R
import com.example.educonnect.databinding.ActivityArticleBinding
import com.example.educonnect.util.Constants.Companion.ARTICLE_IMAGE
import com.example.educonnect.util.Constants.Companion.AUTHOR
import com.example.educonnect.util.Constants.Companion.DESCRIPTION
import com.example.educonnect.util.Constants.Companion.IMAGE
import com.example.educonnect.util.Constants.Companion.PUBLISH_TIME
import com.example.educonnect.util.Constants.Companion.TITLE

class ArticleActivity : AppCompatActivity() {

    lateinit var binding: ActivityArticleBinding

    lateinit var title: String
    lateinit var description: String
    lateinit var publishTime: String
    lateinit var author: String
    lateinit var imageLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setIncomingData()

        binding.back.setOnClickListener {
            finish()
        }

    }

    private fun setIncomingData() {
        title = intent.getStringExtra(TITLE).toString()
        description = intent.getStringExtra(DESCRIPTION).toString()
        publishTime = intent.getStringExtra(PUBLISH_TIME).toString()
        author = intent.getStringExtra(AUTHOR).toString()
        imageLink = intent.getStringExtra(ARTICLE_IMAGE).toString()


        binding.articleTitle.text = title
        binding.articleDescription.text = description
        binding.articlePublishTime.text = publishTime

        Glide.with(this)
            .load(imageLink)
            .into(binding.articleImage)
    }
}