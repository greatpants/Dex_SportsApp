package com.example.yameng.sportsapp.fragment.recycler

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.yameng.sportsapp.R

abstract class FragmentLoadRecycler : FragmentBaseRecycler() {

    private var layManager = LinearLayoutManager(activity)
    private var decoration: RecyclerItemDecoration? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    //默认的距离顶部的间距
    protected var defaultTopDecoration: Int = 0

    //是否显示空View,使用此标志位的原因是使其刚开始加载的时候不显示空View，当首次加载成功后，以后才会显示
    protected var isShowEmptyView = false
    //是否有默认的背景颜色,使用标志位的原因是子类可以选择不设置默认背景颜色来方式出现的过度绘制的问题
    private var isHadDefaultBack = true

    protected val layoutManager: RecyclerView.LayoutManager
        get() = layManager


    // 子类必须要实现的方法
    // 1、得到要显示的item的大小（而不是得到数据源的大小，因为有的界面一个item会对应好几个数据源）
    protected abstract val itemSize: Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isInit) {
            //1、设置分割线
            decoration = RecyclerItemDecoration()
            recyclerView!!.addItemDecoration(decoration!!)

            //2、设置LayoutManager
            layManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView!!.layoutManager = layManager

            //3、设置Adapter(子类可以复写这个Adapter)
            adapter = RecyclerAdapter()
            recyclerView!!.adapter = adapter

            //得到默认距离顶部的间距
            defaultTopDecoration = resources.getDimensionPixelSize(R.dimen.recycler_default_decoration_height)
            //设置背景颜色
            if (isHadDefaultBack) {
                //recyclerView!!.setBackgroundColor(resources.getColor(R.color.recycler_default_bg))
            }
        }

    }

    protected inner class RecyclerItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            setItemOffset(outRect, position)
        }


        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
        }
    }

    override fun onPostRequest(data: FragmentBaseRecycler.RequestResult?, refresh: Boolean) {
        isShowEmptyView = true
        notifyDataSetChanged()
    }

    /**
     * 更新RecyclerView的方法
     */
    protected fun notifyDataSetChanged() {
        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
        }
    }

    protected fun getBaseViewHolder(itemView: View): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(itemView) {

        }
    }

    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int {
            val num = itemSize

            //如果没有数据，那么应该显示EmptyView
            if (num == 0 && isShowEmptyView) {
                return 1
            }

            return if (shouldLoad) {
                num + 1
            } else {
                num
            }
        }

        override fun getItemViewType(position: Int): Int {

            val num = itemSize
            if (num == 0) {
                return TYPE_EMPTY
            }

            return if (position == num) {
                TYPE_FOOTER
            } else {
                TYPE_ITEM
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val holder: RecyclerView.ViewHolder

            if (viewType == TYPE_EMPTY) {

                var mEmptyView = getEmptyView(parent)
                if (mEmptyView == null) {
                    mEmptyView = getDefaultEmptyView(parent)
                }

                holder = getBaseViewHolder(mEmptyView)
                holder.itemView.tag = TYPE_EMPTY
            } else if (viewType == TYPE_ITEM) {
                //从子类中获取对应的Holder
                holder = getViewHolder(parent)
                val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                holder.itemView.layoutParams = params
                holder.itemView.tag = TYPE_ITEM
            } else {
                val footerView = LayoutInflater.from(activity).inflate(R.layout.item_load_more, parent,
                        false)
                footerView.tag = TYPE_FOOTER
                holder = getBaseViewHolder(footerView)
            }
            return holder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder.itemView.tag != null && holder.itemView.tag as Int == TYPE_ITEM) {
                //让子类去绑定数据源
                bindingViewHolder(holder, position)
            }
        }

    }

    /**
     * 得到默认的EmptyView的方法
     *
     * @param parent
     * @return
     */
    protected fun getDefaultEmptyView(parent: ViewGroup): View {
        return View(activity)
    }

    override fun findLastVisibleItemPosition(): Int {
        return layManager.findLastVisibleItemPosition()
    }


    //子类进行配置的方法
    //1、是否画默认的背景
    protected fun hadDefaultBackColor(hadDefaultBack: Boolean) {
        this.isHadDefaultBack = hadDefaultBack
    }

    // 2、得到item之间的间距
    protected abstract fun setItemOffset(outRect: Rect, position: Int)

    // 3、得到子类所对应的Item的ViewHolder
    protected abstract fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    // 4、设置子类所对应的Item的ViewHolder
    protected abstract fun bindingViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    //得到空View，如果想换成其他的空View，子类可以复写这个方法
    fun getEmptyView(parent: ViewGroup): View? {
        return null
    }

    companion object {

        //默认请求的数量
        const val DEFAULT_REQUEST_COUNT = 10
        //第一种为空View的类型,第二种为item的类型，第三种为footer的类型
        protected const val TYPE_EMPTY = 0
        protected const val TYPE_ITEM = 1
        protected const val TYPE_FOOTER = 2
    }

}
