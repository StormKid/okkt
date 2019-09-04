package com.stormkid.okhttpkt.core

import com.stormkid.okhttpkt.rule.WebsocketCallbackRule
import com.stormkid.okhttpkt.utils.GsonFactory
import com.stormkid.okhttpkt.utils.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor

/**
okhttp websocket
@author ke_li
@date 2019/8/15
 */
class OkWebsocket private constructor() {
    companion object {
        val instance by lazy { OkWebsocket() }
    }

    /**
     * 记录config条件如果少于则报错
     */
    private var configCount = 0

    private val builder = OkHttpClientBuilder.Builder

    private val request = Request.Builder()


    inner class Config {
        fun setTimeOut(time: Long) = apply {
            builder.setTimeOut(time)
            configCount += 1
        }

        fun setCookie(isNeed: Boolean) = apply {
            builder.setCookie(isNeed)
        }

        fun showLog(isNeed: Boolean) = apply {
            if (isNeed) {
                builder.addInterceptor(Interceptor {
                    Log.setEnable(true)
                    Log.i("okhttpUrl", it.request().url().toString())
                    it.proceed(it.request())
                })
                builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            } else {
                Log.setEnable(false)
                builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
            }
            configCount += 1
        }

        fun setFollowed(isNeed: Boolean) = apply {
            builder.setFollowRedirects(isNeed)
        }

        fun setHead(headers: HashMap<String, String>) = apply {
            builder.setHead(headers)
        }
    }


    private var url = ""
    private var websocket: WebSocket? = null

    inner class Builder {
        fun setUrl(path: String) = this.apply {
            url = path
        }

        fun build() = this@OkWebsocket
    }


    fun <T> startSocket(websocketCallbackRule: WebsocketCallbackRule<T>) {
        if (configCount < 2) throw RuntimeException("There must init log and connect timeout")
        val customnClient = builder.build().getCustomnClient().build()
        val build = request.url(url).build()
        websocket = customnClient.newWebSocket(build, OkSocketCallback(websocketCallbackRule))
        customnClient.dispatcher().executorService().shutdown()
    }


    fun sendMsg(requestBody: Any) {
        val json = GsonFactory.toJson(requestBody)
        if (websocket != null) {
            runBlocking {
                launch(Dispatchers.IO) {
                    websocket?.send(json)
                }
            }
        }
    }

}