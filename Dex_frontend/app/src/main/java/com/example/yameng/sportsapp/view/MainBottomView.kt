package com.example.yameng.sportsapp.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

import com.example.yameng.sportsapp.R

// 主页底部的View
class MainBottomView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    // 图标
    private val UNSELECTED_IMAGE_BG = intArrayOf(R.mipmap.tab_information_unselected, R.mipmap.tab_sport_unselected, R.mipmap.tab_me_unselected)
    private val SELECTED_IMAGE_BG = intArrayOf(R.mipmap.tab_information_selected, R.mipmap.tab_sport_selected, R.mipmap.tab_me_selected)

    private val mInformationTab: View
    private val mSportTab: View
    private val mMineTab: View
    private val mInformationIv: ImageView
    private val mSportIv: ImageView
    private val mMineIv: ImageView
    private val mImages = arrayOfNulls<ImageView>(TAB_NUM)

    private val mClickListener = ClickListener()

    private var mSelectIndex = DEFAULT_POSITION

    private var mBottomClickListener: OnBottomChooseListener? = null

    init {

        orientation = LinearLayout.HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_main_bottom, this, true)

        mInformationTab = findViewById(R.id.tab_information)
        mSportTab = findViewById(R.id.tab_sport)
        mMineTab = findViewById(R.id.tab_mine)


        mInformationIv = findViewById<View>(R.id.information_iv) as ImageView
        mSportIv = findViewById<View>(R.id.sport_iv) as ImageView
        mMineIv = findViewById<View>(R.id.mine_iv) as ImageView
        mImages[0] = mInformationIv
        mImages[1] = mSportIv
        mImages[2] = mMineIv

        setImageState(mSelectIndex, true)

        mInformationTab.setOnClickListener(mClickListener)
        mSportTab.setOnClickListener(mClickListener)
        mMineTab.setOnClickListener(mClickListener)
    }

    private inner class ClickListener : View.OnClickListener {

        override fun onClick(v: View) {

            if (mBottomClickListener != null) {

                val newSelect: Int
                when (v.id) {
                    R.id.tab_information -> newSelect = 0

                    R.id.tab_sport -> newSelect = 1

                    else -> newSelect = 2
                }

                if (mSelectIndex != newSelect) {
                    setImageState(mSelectIndex, false)
                    setImageState(newSelect, true)

                    mBottomClickListener!!.onClick(mSelectIndex, newSelect)
                    mSelectIndex = newSelect
                }

            }

        }
    }

    private fun setImageState(position: Int, isSelected: Boolean) {
        if(position > 2 || position < 0) return
        if (isSelected) {
            mImages[position]!!.setImageDrawable(ResourcesCompat.getDrawable(resources, SELECTED_IMAGE_BG[position], null))
        } else {
            mImages[position]!!.setImageDrawable(ResourcesCompat.getDrawable(resources, UNSELECTED_IMAGE_BG[position], null))
        }
    }


    interface OnBottomChooseListener {
        fun onClick(oldPositon: Int, position: Int)
    }

    fun setBottomCLickListener(bottomCLickListener: OnBottomChooseListener) {
        this.mBottomClickListener = bottomCLickListener
    }

    companion object {

        //默认选择的位置
        private val DEFAULT_POSITION = 0
        private val TAB_NUM = 3
    }

}
