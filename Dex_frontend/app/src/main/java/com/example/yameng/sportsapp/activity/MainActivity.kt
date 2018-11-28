package com.example.yameng.sportsapp.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

import com.example.yameng.sportsapp.R
import com.example.yameng.sportsapp.fragment.NewsFragment
import com.example.yameng.sportsapp.view.MainBottomView

class MainActivity : AppCompatActivity() {

    private var bottomView: MainBottomView? = null

    private var selectPos = 0

    private val bottomChooseListener = BottomChooseListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        selectPositionFragment(selectPos)
    }

    private fun initView() {
        bottomView = findViewById(R.id.main_bottom_tab)
        bottomView!!.setBottomCLickListener(bottomChooseListener)
    }

    //初始化一个Fragment，并将其添加到指定位置
    private fun initFragment(position: Int) {

        val fragment: Fragment
        when (position) {
            0 -> fragment = NewsFragment()
            1 -> fragment = NewsFragment()
            2 -> fragment = NewsFragment()
            else -> fragment = NewsFragment()
        }
        supportFragmentManager.beginTransaction().add(R.id.main_content_ll, fragment, getFragmentTagByPosition(position)).commit()
    }

    //得到指定Tag的Fragment
    private fun getFragmentByTag(position: Int): Fragment? {
        return supportFragmentManager.findFragmentByTag(getFragmentTagByPosition(position))
    }

    //得到每个Fragment对应的tag
    private fun getFragmentTagByPosition(position: Int): String {
        return FRAGMENT_BASE_TAG + position.toString()
    }

    private fun selectPositionFragment(position: Int) {
        val selectFragment = getFragmentByTag(position)

        if (selectFragment == null) {
            initFragment(position)
        } else {
            supportFragmentManager.beginTransaction().show(selectFragment).commit()
        }
    }


    private inner class BottomChooseListener : MainBottomView.OnBottomChooseListener {

        override fun onClick(oldPosition: Int, selectPosition: Int) {

            val nowFragment = getFragmentByTag(oldPosition)
            supportFragmentManager.beginTransaction().hide(nowFragment!!).commit()
            selectPositionFragment(selectPosition)

            selectPos = selectPosition
        }
    }

    companion object {

        private const val FRAGMENT_BASE_TAG = "base_fragment"
    }


}
