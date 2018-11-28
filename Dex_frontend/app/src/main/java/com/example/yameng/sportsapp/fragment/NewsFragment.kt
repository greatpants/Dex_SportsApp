package com.example.yameng.sportsapp.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

import com.example.yameng.sportsapp.activity.ArticleActivity
import com.example.yameng.sportsapp.fragment.recycler.FragmentBaseRecycler
import com.example.yameng.sportsapp.fragment.recycler.FragmentLoadRecycler
import com.example.yameng.sportsapp.model.News
import com.example.yameng.sportsapp.model.request.NewsRequest
import com.example.yameng.sportsapp.view.NewsItemView
import com.orhanobut.logger.Logger

import java.util.ArrayList

class NewsFragment : FragmentLoadRecycler() {

    //private val mNewsList = ArrayList<News>()
    override val itemSize = 10 //mNewsList.size

    /*
    private fun onItemClick(position: Int) {
        val news = mNewsList[position]
        val articleUrl = news.url

        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra(ArticleActivity.BUNDLE_URL, articleUrl)
        startActivity(intent)
    }
    */

    override fun requestDataInBackground(isRefresh: Boolean): FragmentBaseRecycler.RequestResult {
       /*
        val begin: Int
        if (isRefresh) {
            begin = 0
        } else {
            begin = mNewsList.size
        }

        val request = NewsRequest()
        request.setBeginCount(begin, FragmentLoadRecycler.DEFAULT_REQUEST_COUNT)
        val resultList = request.newsItem

        val result = RequestResult()
        if (resultList != null) {
            mNewsList.addAll(resultList)

            result.ret = RET_SUCCESS
            result.addCount = resultList.size
        } else {
            result.ret = RET_FAILED
        }
        return result
        */
        return RequestResult()
    }

    override fun setItemOffset(outRect: Rect, position: Int) {
        outRect.set(0, defaultTopDecoration, 0, 0)
    }

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = NewsItemView(context, null)
        return getBaseViewHolder(view)
    }

    override fun bindingViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView as NewsItemView
        //val news = mNewsList[position]

        //itemView.setThumbnailSource(news.thumbnail)
        //itemView.setTitleString(news.name)
        //itemView.setContentString(news.simplecontent)
        //itemView.setOnClickListener { onItemClick(position) }
    }

}
