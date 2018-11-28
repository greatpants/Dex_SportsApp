package com.example.yameng.sportsapp.model.request

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.example.yameng.sportsapp.model.network.listener.OnRequestListener

class LoginRequest(private val username: String, private val password: String, private val listener: OnLoginListener?) : BaseRequest() {

    override fun getRequestPath(): String {
        return REQUEST_PATH
    }

    override fun getRequestJsonString(): String {
        val jsonObject = JSONObject()
        jsonObject["uname"] = username
        jsonObject["upwd"] = password
        return jsonObject.toString()
    }

    override fun getRequestListener(): OnRequestListener<String> {
        return object : OnRequestListener<String> {
            override fun onSuccess(s: String) {

                if (listener != null) {
                    val resultJson = JSON.parseObject(s)
                    val code = resultJson.getInteger(BaseRequest.RESULT_KEY)!!
                    if (code == BaseRequest.REQUEST_SUCCESS) {
                        val token = resultJson.getJSONObject("data").getString("token")
                        listener.onSuccess(token)
                    } else {
                        listener.onFail(code)
                    }
                }

            }

            override fun onFail() {
                listener?.onFail(BaseRequest.REQUEST_OTHER_EXCEPTION)
            }
        }
    }

    interface OnLoginListener {
        fun onSuccess(token: String)

        fun onFail(code: Int)
    }

    companion object {

        val REQUEST_WRONG_USERNAME = 1 //没有相应用户名
        val REQUEST_WRONG_PASSWORD = 2 //密码错误

        private val REQUEST_PATH = "Login"
    }
}
