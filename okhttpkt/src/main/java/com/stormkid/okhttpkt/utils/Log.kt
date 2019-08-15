package com.stormkid.okhttpkt.utils

import android.util.Log

/**
自定义logger
@author ke_li
@date 2019/6/14
 */
object Log {
    private const val APP_LOGGER = "APP_LOGGER"
    private var isEnable = false

    fun setEnable(isEnable: Boolean){
        this.isEnable = isEnable
    }

    fun init(isEnable: Boolean){
        this.isEnable = isEnable
    }

    fun w(msg: Any) {
        if (isEnable)
            Log.w(APP_LOGGER, msg.toString())
    }

    fun d(msg: Any) {
        if (isEnable)
            Log.d(APP_LOGGER, msg.toString())
    }

    fun e(msg: Any) {
        if (isEnable)
            Log.e(APP_LOGGER, msg.toString())
    }

    fun i(msg: Any) {
        if (isEnable)
            Log.i(APP_LOGGER, msg.toString())
    }

    fun i(key:String ,msg: Any) {
        if (isEnable)
            Log.i(key, msg.toString())
    }

    fun e(key:String, msg: Any) {
        if (isEnable)
            Log.e(key, msg.toString())
    }

    fun d(key:String, msg: Any) {
        if (isEnable)
            Log.d(key, msg.toString())
    }

    fun w(key:String, msg: Any) {
        if (isEnable)
            Log.w(key, msg.toString())
    }
}