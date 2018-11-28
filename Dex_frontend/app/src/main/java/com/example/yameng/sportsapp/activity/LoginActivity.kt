package com.example.yameng.sportsapp.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import com.example.yameng.sportsapp.R
import com.example.yameng.sportsapp.activity.NewsActivity
import com.example.yameng.sportsapp.model.request.LoginRequest
import com.example.yameng.sportsapp.model.User
import com.example.yameng.sportsapp.utils.DensityUtils
import com.example.yameng.sportsapp.utils.ToastUtils

import kotlinx.android.synthetic.main.activity_login.*
import com.orhanobut.logger.Logger

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    //动画的时长
    private val ANIMATION_TIME = 1000
    //底部Button距中间线的高度
    private val BOTTOM_MARGIN = 50
    private val loginListener = OnLoginListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        //beginAnimation()
    }

    private fun initView() {
        login_bt.setOnClickListener { attemptLogin() }
        register_bt.setOnClickListener{ toRegister() }
    }

    private fun attemptLogin() {
        // Reset errors.
        username_et.error = null
        password_et.error = null

        // Store values at the time of the login attempt.
        val usernameStr = username_et.text.toString()
        val passwordStr = password_et.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password.
        if (TextUtils.isEmpty(passwordStr)) {
            password_et.error = getString(R.string.error_user_pass_empty)
            focusView = password_et
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(usernameStr)) {
            username_et.error = getString(R.string.error_user_pass_empty)
            focusView = username_et
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            //val request = LoginRequest(usernameStr, passwordStr, loginListener)
            //request.doRequest()
            val intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toRegister() {
        val i = Intent(this,RegisterActivity::class.java)
        startActivity(i)
    }

    private inner class OnLoginListener : LoginRequest.OnLoginListener {

        override fun onSuccess(token: String) {

            User.getUser().setUserToken(token)

            //val intent = Intent(this@LoginActivity, NewsActivity::class.java)
            //startActivity(intent)
        }


        override fun onFail(code: Int) {
            Logger.d("失败的code为$code")

            when (code) {

                LoginRequest.REQUEST_WRONG_USERNAME -> ToastUtils.showToast(this@LoginActivity, resources.getString(R.string.error_wrong_username))

                LoginRequest.REQUEST_WRONG_PASSWORD -> ToastUtils.showToast(this@LoginActivity, resources.getString(R.string.error_wrong_password))

                else -> ToastUtils.showToast(this@LoginActivity, resources.getString(R.string.error_request))
            }
        }
    }

    /**
     * 开始动画
     */
    /*
    private fun beginAnimation() {

        val d = getResources().getDisplayMetrics()
        val screenHeight = d.heightPixels

        var TopLl = View(this)
        var BottomLl = View(this)
        TopLl.post(Runnable {
            val topLlHeight = TopLl.getMeasuredHeight()
            val animationHeight = screenHeight / 2 - topLlHeight

            val holder1 = PropertyValuesHolder.ofFloat("translationY", animationHeight)
            val holder2 = PropertyValuesHolder.ofFloat("alpha", 0.3f, 1.0f)

            val animator = ObjectAnimator.ofPropertyValuesHolder(TopLl, holder1, holder2)
            animator.duration = ANIMATION_TIME.toLong()
            animator.start()
        })

        BottomLl.post(Runnable {
            val animationHeight = screenHeight / 2 + DensityUtils.dp2px(this, BOTTOM_MARGIN) - BottomLl.getTop()

            val holder1 = PropertyValuesHolder.ofFloat("translationY", animationHeight)
            val holder2 = PropertyValuesHolder.ofFloat("alpha", 0.3f, 1.0f)

            val animator = ObjectAnimator.ofPropertyValuesHolder(BottomLl, holder1, holder2)
            animator.duration = ANIMATION_TIME.toLong()
            animator.start()
        })
    }
    */
}
