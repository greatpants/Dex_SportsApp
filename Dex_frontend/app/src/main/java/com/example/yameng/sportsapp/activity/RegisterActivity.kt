package com.example.yameng.sportsapp.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.content.Intent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import com.example.yameng.sportsapp.R
import com.example.yameng.sportsapp.model.request.RegisterRequest
import com.example.yameng.sportsapp.utils.ToastUtils.showToast

public class RegisterActivity : AppCompatActivity() {
    private var cancel = false
    private var focusView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // 注意顺序
        setContentView(R.layout.activity_register)
        //window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.fragment_title);
        initView()
    }

    private fun initView() {
        register_bt.setOnClickListener { attemptSignUp() }

        email_et.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                cancel = false
            }
            else {
                if (!isEmailValid(email_et.text.toString())) {
                    email_et.error = getString(R.string.error_invalid_email)
                    cancel = true
                }
            }
        }

        username_et.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                cancel = false
            }
            else {
                if(!isUsernameValid(username_et.text.toString())) {
                    username_et.error = getString(R.string.error_invalid_username)
                    focusView = username_et
                    cancel = true
                }
            }
        }

        password_et.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                cancel = false
            }
            else {
                if (!isPasswordValid(password_et.text.toString())) {
                    password_et.error = getString(R.string.error_invalid_password)
                    focusView = password_et
                    cancel = true
                }
            }
        }

        password_confirm_et.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                cancel = false
            }
            else {
                if (password_et.text.toString() != password_confirm_et.text.toString()) {
                    password_confirm_et.error = getString(R.string.error_pass_confirm)
                    focusView = password_confirm_et
                    cancel = true
                }
            }
        }
    }

    private fun attemptSignUp() {
        // Reset errors.
        email_et.error = null
        password_et.error = null
        password_confirm_et.error = null
        username_et.error = null

        // Store values at the time of the login attempt.
        val emailStr = email_et.text.toString()
        val passwordStr = password_et.text.toString()
        val passwordConfirmStr = password_confirm_et.text.toString()
        val usernameStr = username_et.text.toString()

        // Check for a valid username.
        if (TextUtils.isEmpty(usernameStr)) {
            username_et.error = getString(R.string.error_user_empty)
            focusView = username_et
            cancel = true
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(passwordStr)) {
            password_et.error = getString(R.string.error_pass_empty)
            focusView = password_et
            cancel = true
        }

        // Check for a valid password confirming.
        if (TextUtils.isEmpty(passwordConfirmStr)) {
            password_confirm_et.error = getString(R.string.error_pass_confirm_empty)
            focusView = password_confirm_et
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email_et.error = getString(R.string.error_invalid_email)
            focusView = email_et
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
            cancel = false
        } else {
            //TODO: 将数据存入后台
            /*
            val request = RegisterRequest(usernameStr, passwordStr, emailStr, object : RegisterRequest.onRegisterListener {
                override fun onSuccess() {
                    showToast(this@RegisterActivity, getString(R.string.register_success))
                    finish()
                }

                override fun onRepeatUserName() {
                    showToast(this@RegisterActivity, getString(R.string.error_repeat_user_name))
                }

                override fun onFail() {
                    showToast(this@RegisterActivity, getString(R.string.error_request))
                }
            })
            request.doRequest()
            */
            // 提示注册成功
            Toast.makeText(this, "注册成功!", Toast.LENGTH_LONG).show()
            // 跳转至登录界面
            val intent = Intent()
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\\.)+(com|cn|net|org)\$")
        return regex.matches(email)
    }

    private fun isPasswordValid(password: String): Boolean {
        var isDigit = false //是否包含数字
        var isLetter = false //是否包含字母
        for (i in password.indices) {
            if (Character.isDigit(password[i])) {
                isDigit = true
            } else if (Character.isLetter(password[i])) {
                isLetter = true
            }
        }
        val regex = Regex("^[a-zA-Z0-9]{6,}$")
        return isDigit && isLetter && regex.matches(password)
    }

    private fun isUsernameValid(username: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9]$")
        return regex.matches(username)
    }
}
