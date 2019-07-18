package com.stormkid.okhttpkt.core

import com.stormkid.okhttpkt.rule.TestCallbackRule
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
class TestCallback(private val callbackRule: TestCallbackRule) : Callback {


    override fun onFailure(call: Call?, e: IOException) {
        runBlocking { launch(Dispatchers.Main) { callbackRule.onErr(e.message?:"unknow error") } }
        call!!.cancel()
    }

    override fun onResponse(call: Call?, response: Response?) {
        var result = ""
        val heads = hashMapOf<String,String>()
        if (null!=response) {
            result = response.body().toString()
            response.headers().names().forEach {
                val value = response.headers().get(it)
                heads[it] = value?:""
            }
        }
        val back = TestCallbackRule.Response(result,heads)
        runBlocking { launch(Dispatchers.Main) { callbackRule.onResponse(back) } }
        call!!.cancel()
    }



}