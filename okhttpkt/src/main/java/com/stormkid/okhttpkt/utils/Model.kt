package com.stormkid.okhttpkt.utils

import android.content.Context

/**
请求传参
@author ke_li
@date 2018/5/25
 */
data class CallbackNeed (val flag :String, val err_msg:String)

data class FileCallbackNeed(val selfPath:String,val context: Context,var initTotal:Long)