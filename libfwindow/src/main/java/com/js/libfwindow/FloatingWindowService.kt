package com.js.libfwindow

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.*

/**
 * Author: double.
 * CreateTime: 19-9-10 上午11:51.
 * Description:
 */
class FloatingWindowService : Service() {
    private lateinit var mWindowManager: WindowManager
    private lateinit var mParams: WindowManager.LayoutParams
    private var mTouchX = 0f
    private var mTouchY = 0f
    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private lateinit var mView: View
    var isOpen = false
    var isCanMove = true
    override fun onBind(intent: Intent?): IBinder? {
        return MyBinder()
    }

    override fun onCreate() {
        super.onCreate()

        initWindow()
    }

    private fun initWindow() {
        mParams = WindowManager.LayoutParams()
        mWindowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mParams.type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }

        mParams.format = PixelFormat.RGBA_8888
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        mParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN

//        //设置窗口初始停靠位置.
        mParams.gravity = Gravity.LEFT or Gravity.TOP
        initData()
    }

    private fun initData() {
        val dm = DisplayMetrics()
        mWindowManager.defaultDisplay.getMetrics(dm)
        mScreenWidth = dm.widthPixels
        mScreenHeight = dm.heightPixels
    }

    internal fun addView(view: View, x: Int, y: Int) {
        mView = view
        view.setOnTouchListener { v, event ->
            if (isCanMove) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mTouchX = event.x
                        mTouchY = event.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        updateView((event.rawX - mTouchX).toInt(), (event.rawY - mTouchY).toInt())
                    }
                }
            }
            false
        }

        isOpen = true
        if (view.measuredHeight == 0 && view.measuredWidth == 0) {
            view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        //默认左下角
        mParams.width = view.measuredWidth
        mParams.height = view.measuredHeight
        mParams.y = y
        mParams.x = x
        Log.e("js", "mParams:${mParams.toString()} ${mParams.width}")
        mWindowManager.addView(view, mParams)
    }

    fun getParams(): WindowManager.LayoutParams {
        return mParams
    }

    internal fun addView(view: View) {
        addView(view, 0, mScreenHeight)
    }

    internal fun updateView(x: Int, y: Int) {
        mParams.y = y
        mParams.x = x
        mWindowManager.updateViewLayout(mView, mParams)
    }

    internal fun updateViewWH(width: Int, height: Int) {
        mParams.height = height
        mParams.width = width
        mWindowManager.updateViewLayout(mView, mParams)
    }

    internal fun closeWindow() {
        isOpen = false
        mView ?: return
        mWindowManager.removeView(mView)
    }

    inner class MyBinder : Binder() {
        fun getService(): FloatingWindowService {
            return this@FloatingWindowService
        }
    }


}