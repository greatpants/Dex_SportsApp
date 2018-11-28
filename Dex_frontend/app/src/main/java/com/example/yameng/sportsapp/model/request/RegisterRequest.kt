package com.example.yameng.sportsapp.model.request

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.example.yameng.sportsapp.model.network.listener.OnRequestListener

class RegisterRequest(private val mUsername: String, private val mPassword: String, private val mEmail: String, private val mListener: onRegisterListener) : BaseRequest() {

    override fun getRequestListener(): OnRequestListener<String> {
        return object : OnRequestListener<String> {
            override fun onSuccess(s: String) {
                val resultJson = JSON.parseObject(s)
                val code = resultJson.getInteger(BaseRequest.RESULT_KEY)!!

                //根据不同的code，回调不同的接口的方法
                if (code == BaseRequest.REQUEST_SUCCESS) {
                    mListener.onSuccess()
                } else if (code == REQUEST_REPEAT_USERNAME) {
                    mListener.onRepeatUserName()
                } else {
                    mListener.onFail()
                }
            }

            override fun onFail() {
                mListener.onFail()
            }
        }
    }

    override fun getRequestPath(): String {
        return REQUEST_PATH
    }

    override fun getRequestJsonString(): String {
        val jsonObject = JSONObject()
        jsonObject["uname"] = mUsername
        jsonObject["upwd"] = mPassword
        jsonObject["ueml"] = mEmail
        return jsonObject.toJSONString()
    }

    interface onRegisterListener {
        fun onSuccess()

        fun onRepeatUserName()

        fun onFail()
    }

    companion object {

        private val REQUEST_PATH = "Register"

        //重复用户名
        private val REQUEST_REPEAT_USERNAME = 1
    }
}
