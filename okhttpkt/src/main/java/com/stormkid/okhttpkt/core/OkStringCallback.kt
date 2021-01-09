package com.stormkid.okhttpkt.core

import com.stormkid.okhttpkt.rule.StringCallback
import com.stormkid.okhttpkt.utils.CallbackNeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


/**
okhttp String请求回调
@author ke_li
@date 2018/5/25
 */
class OkStringCallback(private val callbackRule: StringCallback, private val need: CallbackNeed) :
    Callback {


    override fun onFailure(call: Call, e: IOException) {
        runBlocking { launch(Dispatchers.Main) { callbackRule.onFailed(need.err_msg) } }
        call.cancel()
    }

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {
            if (null == response.body) {
                runBlocking { launch(Dispatchers.Main) { callbackRule.onFailed(need.err_msg) } }
                return
            } else {
                val body = response.body?.string() ?: ""
                //获取接口上的泛型注入
                runBlocking { launch(Dispatchers.Main) { callbackRule.onSuccess(body, need.flag) } }
            }
        } else runBlocking { launch(Dispatchers.Main) { callbackRule.onFailed(response.message) } }
        call.cancel()
    }


}