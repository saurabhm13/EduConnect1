package com.example.educonnect.ui.activity.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.educonnect.R
import com.example.educonnect.databinding.FragmentHomeBinding
import com.example.educonnect.ui.activity.ArticleActivity
import com.example.educonnect.ui.activity.MainActivity
import com.example.educonnect.ui.activity.SingleChatActivity
import com.example.educonnect.ui.activity.VideoActivity
import com.example.educonnect.ui.adapter.ArticlesAdapter
import com.example.educonnect.ui.adapter.VideoAdapter
import com.example.educonnect.ui.viewmodel.MainViewModel
import com.example.educonnect.util.Constants
import com.example.educonnect.util.Constants.Companion.ARTICLE_IMAGE
import com.example.educonnect.util.Constants.Companion.AUTHOR
import com.example.educonnect.util.Constants.Companion.DESCRIPTION
import com.example.educonnect.util.Constants.Companion.ID
import com.example.educonnect.util.Constants.Companion.IMAGE
import com.example.educonnect.util.Constants.Companion.NAME
import com.example.educonnect.util.Constants.Companion.PUBLISH_TIME
import com.example.educonnect.util.Constants.Companion.TITLE
import com.example.educonnect.util.Constants.Companion.VIDEO_LINK

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: MainViewModel
    lateinit var articlesAdapter: ArticlesAdapter
    lateinit var videoAdapter: VideoAdapter

    var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

//         If user comes from notification

        if (activity?.intent?.getStringExtra("openChatFragment") != null) {
            val userId = activity?.intent?.getStringExtra(ID)
            val name = activity?.intent?.getStringExtra(NAME)
            val image = activity?.intent?.getStringExtra(IMAGE)

            val intoSingleChat = Intent(activity, SingleChatActivity::class.java)
            intoSingleChat.putExtra(ID, userId)
            intoSingleChat.putExtra(NAME, name)
            intoSingleChat.putExtra(IMAGE, image)
            intoSingleChat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intoSingleChat)
        }

        viewModel.getArticles()
        prepareArticlesRecyclerView(R.id.chip_articles)
        viewModel.observeArticlesLiveData().observe(viewLifecycleOwner) {
            articlesAdapter.setArticleList(it)
        }

        binding.chipGroupList.setOnCheckedChangeListener {group, checkedId ->
            prepareArticlesRecyclerView(checkedId)
        }

        viewModel.getFeaturedImage()
        viewModel.observeFeaturedImageLiveData().observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it.image)
                .into(binding.featuredImage)
        }

//        binding.chipGroupList.setOnCheckedStateChangeListener {group, checkedId ->
//            prepareArticlesRecyclerView(checkedId)
//        }

        return binding.root

    }

    private fun prepareArticlesRecyclerView(checkedId: Int) {

        when (checkedId) {
            R.id.chip_articles -> {

                binding.rvArticles.visibility = View.VISIBLE
                binding.rvVideos.visibility = View.GONE

                viewModel.getArticles()
                viewModel.observeArticlesLiveData().observe(viewLifecycleOwner) {
                    articlesAdapter.setArticleList(it)
                }

                articlesAdapter = ArticlesAdapter {
                    val intoArticleDetail = Intent(activity, ArticleActivity::class.java)
                    intoArticleDetail.putExtra(TITLE, it.title)
                    intoArticleDetail.putExtra(DESCRIPTION, it.description)
                    intoArticleDetail.putExtra(AUTHOR, it.author)
                    intoArticleDetail.putExtra(ARTICLE_IMAGE, it.imageLink)
                    intoArticleDetail.putExtra(PUBLISH_TIME, it.publishedTime)
                    startActivity(intoArticleDetail)
                }

                binding.rvArticles.apply {
                    layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    adapter = articlesAdapter
                }
            }
            R.id.chip_videos -> {

                binding.rvArticles.visibility = View.GONE
                binding.rvVideos.visibility = View.VISIBLE

                viewModel.getVideos()
                viewModel.observeVideosLiveData().observe(viewLifecycleOwner) {
                    videoAdapter.setVideoList(it)
                }

                videoAdapter = VideoAdapter {
                    val intoVideoDetail = Intent(activity, VideoActivity::class.java)
                    intoVideoDetail.putExtra(TITLE, it.title)
                    intoVideoDetail.putExtra(DESCRIPTION, it.description)
                    intoVideoDetail.putExtra(AUTHOR, it.author)
                    intoVideoDetail.putExtra(ARTICLE_IMAGE, it.imageLink)
                    intoVideoDetail.putExtra(PUBLISH_TIME, it.publishedTime)
                    intoVideoDetail.putExtra(VIDEO_LINK, it.videoLink)
                    startActivity(intoVideoDetail)
                }

                binding.rvVideos.apply {
                    layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    adapter = videoAdapter
                }

            }
        }
    }


}