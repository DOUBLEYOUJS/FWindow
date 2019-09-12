package com.js.libfwindow

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.view.View

/**
 * Author: double.
 * CreateTime: 19-9-10 上午11:38.
 * Description:
 */
class FloatingWindow {
    private lateinit var mService: FloatingWindowService
    private var mContext: Context? = null

    companion object {
        val INSTANCE: FloatingWindow = Helper.instance
    }

    private object Helper {
        val instance = FloatingWindow()
    }

    private val mServiceConnectin: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = (service as FloatingWindowService.MyBinder).getService()
        }

    }

    /**
     * 添加view
     */
    fun addView(view: View) {
        mService.addView(view)
    }

    /**
     * 添加view
     */
    fun addView(view: View, x: Int, y: Int) {
        mService.addView(view, x, y)
    }

    /**
     * 添加view
     */
    fun addView(view: View, x: Int, y: Int, width: Int, height: Int) {
        mService.addView(view, x, y)
        updateWindowWH(width, height)
    }

    fun getX(): Int {
        return mService.getParams().x
    }

    fun getY(): Int {
        return mService.getParams().y
    }

    /**
     * 更新View的位置
     */
    fun updateWindowSite(x: Int, y: Int) {
        mService.updateView(x, y)
    }

    /**
     * 更新window宽高
     */
    fun updateWindowWH(width: Int, height: Int) {
        mService.updateViewWH(width, height)
    }

    /**
     * 初始化Window
     */
    fun initWindow(context: Context) {
        mContext = context
        val intent = Intent(context, FloatingWindowService::class.java)
        context.bindService(intent, mServiceConnectin, Context.BIND_AUTO_CREATE)
    }

    /**
     * 关闭Window
     */
    fun closeWindow() {
        mService.stopService()
        mContext?.unbindService(mServiceConnectin)
    }
}