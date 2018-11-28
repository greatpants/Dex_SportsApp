package com.example.yameng.sportsapp

import android.app.Application

import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.common.Logger
import com.example.yameng.sportsapp.model.User
import com.example.yameng.sportsapp.model.network.HttpClient


/**
 * 程序的Application对象
 * Created by liuheng on 16/6/7.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //可以在Application对象中初始化一些类库

        //Logger.init(TAG_NAME);
        HttpClient.init(this)

        User.init(applicationContext)

        SDKInitializer.initialize(applicationContext)
    }

    companion object {

        private val TAG_NAME = "MY_TAG"
    }
}
