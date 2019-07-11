package com.stormkid.okhttpkt.core

import com.stormkid.okhttpkt.cache.CookieCaches
import com.stormkid.okhttpkt.cache.CookieManager
import com.stormkid.okhttpkt.rule.ClientRule
import com.stormkid.okhttpkt.rule.FactoryRule
import com.stormkid.okhttpkt.utils.HttpsUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
@author ke_li
@date 2018/5/23
 */
class OkHttpClientBuilder private constructor() : ClientRule {


    private val logBody = HttpLoggingInterceptor.Level.BODY
    private val logNone = HttpLoggingInterceptor.Level.NONE
    /**
     * 超时
     */
    private var ERR_TIME = 5000L

    /**
     * 是否需要处理cookie
     */
    private var IS_NEED_COOKIE = false

    companion object {
        private val httpClient: OkHttpClient.Builder by lazy { OkHttpClient.Builder() }
        private val heads = hashMapOf<String, String>()
        private var showLog = false
        private val instance: OkHttpClientBuilder by lazy { OkHttpClientBuilder() }
    }

    /**
     * 自定义httpclient 时调用的类，以及可以作为mvp 参考
     */
    object Builder : FactoryRule {
        override fun setCookie(isNeed: Boolean) {
            if (isNeed)
            httpClient.cookieJar(CookieCaches(CookieManager.instance))
        }

        override fun setTimeOut(time: Long) {
            httpClient.readTimeout(time, TimeUnit.MILLISECONDS)
            httpClient.writeTimeout(time, TimeUnit.MILLISECONDS)
            httpClient.readTimeout(time, TimeUnit.MILLISECONDS)
        }

        override fun addInterceptor(interceptor: Interceptor): FactoryRule = this.apply {
            httpClient.addInterceptor(interceptor)
        }

        override fun addNetworkInterceptor(interceptor: Interceptor): FactoryRule = this.apply {
            httpClient.addNetworkInterceptor(interceptor)
        }

        override fun writeTimeOut(time: Long): FactoryRule = this.apply {
            httpClient.writeTimeout(time, TimeUnit.MILLISECONDS)
        }

        override fun socketFactory(socketFactory: SocketFactory): FactoryRule = this.apply {
            httpClient.socketFactory(socketFactory)
        }

        override fun SSLSocketFactory(sslSocketFactory: SSLSocketFactory, x509TrustManager: X509TrustManager): FactoryRule = this.apply {
            httpClient.sslSocketFactory(sslSocketFactory, x509TrustManager)
        }

        override fun dns(dns: Dns): FactoryRule = this.apply {
            httpClient.dns(dns)
        }

        override fun cache(cache: Cache): FactoryRule = this.apply {
            httpClient.cache(cache)
        }

        fun setHead(hashMap: HashMap<String, String>) = this.apply {
            heads.clear()
            heads.putAll(hashMap)
        }

        fun build(): OkHttpClientBuilder {
            return OkHttpClientBuilder.instance
        }
    }


    /**
     * 获取默认httpclient
     */
    override fun getHttpClient() = httpClient.apply {
        /**
         * 清理interceptors 防止重复添加
         */
        interceptors().clear()
        if (heads.size != 0) addInterceptor {
            val myhead = Headers.of(heads)
            val builder = it.request().newBuilder()
            it.proceed(builder.headers(myhead).build())
        }

        if (showLog) {
            addInterceptor {
                android.util.Log.i("okhttpUrl", it.request().url().toString())
                it.proceed(it.request())
            }
            addInterceptor(HttpLoggingInterceptor().setLevel(logBody))
        } else addInterceptor(HttpLoggingInterceptor().setLevel(logNone))

        if (IS_NEED_COOKIE)  httpClient.cookieJar(CookieCaches(CookieManager.instance))
        connectTimeout(ERR_TIME, TimeUnit.MILLISECONDS)
        readTimeout(ERR_TIME, TimeUnit.MILLISECONDS)
        writeTimeout(ERR_TIME, TimeUnit.MILLISECONDS)
    }

    /**
     * 获取默认httpsclient
     */
    override fun getHttpsClient() = httpClient.apply {
        getHttpClient().apply {
            val ssl = HttpsUtils.getSslSocketFactory()
            sslSocketFactory(ssl.sSLSocketFactory, ssl.trustManager)
        }
    }

    /**
     * 获取自定义的client
     */
    override fun getCustomnClient() = httpClient


    /**
     * 是否显示log
     */
    override fun isLogShow(isShow: Boolean) {
        showLog = isShow
    }

    /**
     * 规定以毫秒做单位
     */
    override fun setTimeOut(time: Long) {
        ERR_TIME = time
    }

    /**
     * 是否需要cookie
     */
    override fun isNeedCookie(isNeed: Boolean) {
        IS_NEED_COOKIE = isNeed
    }


    override fun setFollowRedirects(allowRedirect: Boolean) {
        httpClient.followRedirects(allowRedirect)
    }

}