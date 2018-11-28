package com.example.yameng.sportsapp.fragment.recycler

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.yameng.sportsapp.R


/**
 * 下拉刷新使用的RecyclerView的父类
 */
abstract class FragmentBaseRecycler : Fragment() {

    private var contentView: View? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    protected var recyclerView: RecyclerView? = null

    private val scrollListener = ScrollListener()
    private val refreshListener = RefreshListener()

    protected var isInit = true
    //是否正在请求的标志位
    private var isLoad = false
    //是否有更多的标志位
    protected var shouldLoad = true


    //子类来控制父类行为的方法
    // 是否可以下拉刷新
    private val isRefreshEnable: Boolean
        get() = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_base_recycler, container, false)
            refreshLayout = contentView!!.findViewById<View>(R.id.refresh_layout) as SwipeRefreshLayout
            recyclerView = contentView!!.findViewById<View>(R.id.recycler_view) as RecyclerView

            //设置是否下拉刷新
            refreshLayout!!.isEnabled = isRefreshEnable
            if (isRefreshEnable) {
                refreshLayout!!.isRefreshing = true
            }
            refreshLayout!!.setOnRefreshListener(refreshListener)
            recyclerView!!.addOnScrollListener(scrollListener)

            //刚开始进入时，要进行刷新操作
            load(true)
        } else {
            isInit = false
        }
        return contentView
    }


    //进行刷新的方法
    protected fun refresh(isShowSwipeRefreshView: Boolean) {
        if (isShowSwipeRefreshView) {
            refreshLayout!!.isRefreshing = true
        }
        load(true)
    }

    //从开始加载，或者从后面进行加载的方法
    protected fun load(isRefresh: Boolean) {
        if (!isLoad) {
            val loadTask = LoadAsyncTask(isRefresh)
            loadTask.execute()
        }
    }


    //SwipeRefreshLayout刷新的实现类
    private inner class RefreshListener : SwipeRefreshLayout.OnRefreshListener {

        override fun onRefresh() {
            if (isLoad) {
                refreshLayout!!.isRefreshing = false
            } else {
                load(true)
            }

        }
    }

    //RecyclerView滑动时回调的实现类
    private inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val lastVisibleItem = findLastVisibleItemPosition()
            val totalItemCount = recyclerView.layoutManager!!.itemCount
            //当距离底部小于约定数且有下一页时，才会进行刷新
            if (totalItemCount - lastVisibleItem <= DEFAULT_RESET_ITEM_COUNT && shouldLoad) {
                load(false)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

        }
    }

    /**
     * 设置SwipeRefreshLayout下拉刷新时结束的位置
     */
    protected fun setSwipeRefreshEnd(end: Int) {
        setSwipeRefreshStartAndEnd(0, end)
    }

    /**
     * 设置SwipeRefreshLayout下拉刷新时开始的位置和结束的位置
     */
    protected fun setSwipeRefreshStartAndEnd(start: Int, end: Int) {
        refreshLayout!!.setProgressViewOffset(false, start, end)
    }

    //每次都进行加载的AsyncTask
    protected inner class LoadAsyncTask(private val mIsRefresh: Boolean) : AsyncTask<Void, Void, RequestResult>() {


        override fun doInBackground(vararg params: Void): RequestResult {
            isLoad = true
            return requestDataInBackground(mIsRefresh)
        }

        override fun onPostExecute(requestResult: RequestResult?) {

            isLoad = false

            //将mRefreshLayout消失　
            refreshLayout!!.isRefreshing = false

            if (requestResult != null && requestResult.ret == RET_SUCCESS && requestResult.addCount != 0) {
                shouldLoad = true
            } else {
                shouldLoad = false
            }

            //这个应该子类去实现
            onPostRequest(requestResult, mIsRefresh)
        }
    }

    inner class RequestResult {
        var ret = RET_FAILED
        var addCount = ADD_COUNT_EMPTY
    }


    //子类需要实现的方法
    protected abstract fun requestDataInBackground(isRefresh: Boolean): RequestResult

    protected abstract fun onPostRequest(data: RequestResult?, refresh: Boolean)

    protected abstract fun findLastVisibleItemPosition(): Int

    companion object {
        //默认还有多少item到底部时需要加载更多
        private const val DEFAULT_RESET_ITEM_COUNT = 3
        const val RET_SUCCESS = 0
        const val RET_FAILED = -1
        const val ADD_COUNT_EMPTY = 0
    }

}
