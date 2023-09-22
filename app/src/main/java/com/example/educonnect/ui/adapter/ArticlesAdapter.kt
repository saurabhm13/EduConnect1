package com.example.educonnect.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.educonnect.data.Article
import com.example.educonnect.databinding.ArticlesItemListBinding

class ArticlesAdapter(
    private val onItemClick: (Article) -> Unit
): RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {

    private val articleList = ArrayList<Article>()

    @SuppressLint("NotifyDataSetChanged")
    fun setArticleList(articleList: List<Article>) {
        this.articleList.clear()
        this.articleList.addAll(articleList)
        notifyDataSetChanged()
    }

    class ArticlesViewHolder(val binding: ArticlesItemListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder(ArticlesItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(articleList[position].imageLink)
            .into(holder.binding.articleImage)

        holder.binding.articleTitle.text = articleList[position].title
        holder.binding.articleDescription.text = articleList[position].description

        holder.binding.root.setOnClickListener {
            onItemClick.invoke(articleList[position])
        }
    }


}