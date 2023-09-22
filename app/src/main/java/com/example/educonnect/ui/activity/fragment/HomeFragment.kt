package com.example.educonnect.ui.activity.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.educonnect.R
import com.example.educonnect.databinding.FragmentHomeBinding
import com.example.educonnect.ui.activity.ArticleActivity
import com.example.educonnect.ui.activity.MainActivity
import com.example.educonnect.ui.adapter.ArticlesAdapter
import com.example.educonnect.ui.adapter.VideoAdapter
import com.example.educonnect.ui.viewmodel.MainViewModel
import com.example.educonnect.util.Constants.Companion.ARTICLE_IMAGE
import com.example.educonnect.util.Constants.Companion.AUTHOR
import com.example.educonnect.util.Constants.Companion.DESCRIPTION
import com.example.educonnect.util.Constants.Companion.PUBLISH_TIME
import com.example.educonnect.util.Constants.Companion.TITLE
import com.example.educonnect.util.Constants.Companion.VIDEO_LINK

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: MainViewModel
    lateinit var articlesAdapter: ArticlesAdapter
    lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        viewModel.getArticles()
        prepareArticlesRecyclerView(R.id.chip_articles)
        viewModel.observeArticlesLiveData().observe(viewLifecycleOwner) {
            articlesAdapter.setArticleList(it)
        }

        binding.chipGroupList.setOnCheckedChangeListener {group, checkedId ->
            prepareArticlesRecyclerView(checkedId)
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

//                binding.rvArticles.visibility = View.GONE
//                binding.rvVideos.visibility = View.VISIBLE
//
//                viewModel.getVideos()
//                viewModel.observeVideosLiveData().observe(viewLifecycleOwner) {
//                    videoAdapter.setVideoList(it)
//                }

                videoAdapter = VideoAdapter {
                    val intoVideoDetail = Intent(activity, ArticleActivity::class.java)
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