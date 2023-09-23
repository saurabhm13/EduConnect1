package com.example.educonnect.ui.activity

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import com.bumptech.glide.Glide
import com.example.educonnect.databinding.ActivityVideoBinding
import com.example.educonnect.util.Constants.Companion.ARTICLE_IMAGE
import com.example.educonnect.util.Constants.Companion.AUTHOR
import com.example.educonnect.util.Constants.Companion.DESCRIPTION
import com.example.educonnect.util.Constants.Companion.PUBLISH_TIME
import com.example.educonnect.util.Constants.Companion.TITLE
import com.example.educonnect.util.Constants.Companion.VIDEO_LINK

class VideoActivity : AppCompatActivity() {

    lateinit var binding: ActivityVideoBinding

    lateinit var videoTitle: String
    lateinit var videoDescription: String
    lateinit var videoPublishTime: String
    lateinit var videoAuthor: String
    lateinit var imageLink: String
    lateinit var videoLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setIncomingData()

        binding.backVideo.setOnClickListener {
            finish()
        }

        // Set up the Firebase Storage reference to your video
        val videoUri = Uri.parse(videoLink)

        // Create a media controller to add play, pause, and seek functionality
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.video)
        binding.video.setMediaController(mediaController)

        // Set the video URI
        binding.video.setVideoURI(videoUri)

        // Add an OnPreparedListener to hide the progress bar when video starts playing
        binding.video.setOnPreparedListener { mediaPlayer ->
            binding.progressBar.visibility = View.GONE
            binding.backVideo.visibility = View.GONE
            binding.image.visibility = View.GONE
            mediaPlayer.start() // Start playing the video after it's prepared
        }

        // Add an OnClickListener to show/hide the overlay image
        binding.video.setOnClickListener {
            if (binding.backVideo.visibility == View.VISIBLE) {
                binding.backVideo.visibility = View.INVISIBLE
            } else {
                binding.backVideo.visibility = View.VISIBLE
            }
        }

        binding.video.setOnInfoListener { mp, what, extra ->
            when (what) {
                MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                    // Show the progress bar during buffering
                    binding.progressBar.visibility = View.VISIBLE
                    binding.backVideo.visibility = View.VISIBLE
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                    // Hide the progress bar when buffering is complete
                    binding.progressBar.visibility = View.GONE
                    binding.backVideo.visibility = View.GONE
                }

            }
            true
        }

    }

    private fun setIncomingData() {
        videoTitle = intent.getStringExtra(TITLE).toString()
        videoDescription = intent.getStringExtra(DESCRIPTION).toString()
        videoPublishTime = intent.getStringExtra(PUBLISH_TIME).toString()
        videoAuthor = intent.getStringExtra(AUTHOR).toString()
        imageLink = intent.getStringExtra(ARTICLE_IMAGE).toString()
        videoLink = intent.getStringExtra(VIDEO_LINK).toString()

        binding.titleVideo.text = videoTitle
        binding.descriptionVideo.text = videoDescription
        binding.publishTimeVideo.text = videoPublishTime

        Glide.with(this)
            .load(imageLink)
            .into(binding.image)
    }
}