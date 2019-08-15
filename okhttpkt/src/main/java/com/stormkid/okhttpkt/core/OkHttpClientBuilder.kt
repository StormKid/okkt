package com.stormkid.okhttpkt.core

import com.stormkid.okhttpkt.cache.CookieCaches
import com.stormkid.okhttpkt.cache.CookieManager
import com.stormkid.okhttpkt.rule.ClientRule
import com.stormkid.okhttpkt.rule.FactoryRule
import com.stormkid.okhttpkt.utils.HttpsUtils
import com.stormkid.okhttpkt.utils.Log
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

    /**
     * 是否需要重定向
     */
    private var IS_REDIRECT_ALLOW = true

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

        /**
         * 清理拦截器
         */
        override fun cleanInterceptor() = this.apply {
            httpClient.interceptors().clear()
        }

        /**
         * 添加cookie
         */
        override fun setCookie(isNeed: Boolean) = this.apply {
            if (isNeed)
                httpClient.cookieJar(CookieCaches(CookieManager.instance))
        }

        /**
         *  设置超时时间
         */
        override fun setTimeOut(time: Long) = this.apply{
            httpClient.readTimeout(time, TimeUnit.MILLISECONDS)
            httpClient.writeTimeout(time, TimeUnit.MILLISECONDS)
            httpClient.readTimeout(time, TimeUnit.MILLISECONDS)
        }

        /**
         * 添加拦截器
         */
         fun addInterceptor(interceptor: Interceptor) = this.apply {
            httpClient.addInterceptor(interceptor)
        }

        /**
         * 添加请求拦截器
         */
         fun addNetworkInterceptor(interceptor: Interceptor) = this.apply {
            httpClient.addNetworkInterceptor(interceptor)
        }

        /**
         *  只添加写入时间
         */
        override fun writeTimeOut(time: Long) = this.apply {
            httpClient.writeTimeout(time, TimeUnit.MILLISECONDS)
        }

        /**
         *  添加socket 请求
         */
        override fun socketFactory(socketFactory: SocketFactory) = this.apply {
            httpClient.socketFactory(socketFactory)
        }

        /**
         * 自定义添加请求认证证书
         */
        override fun SSLSocketFactory(
            sslSocketFactory: SSLSocketFactory,
            x509TrustManager: X509TrustManager
        ) = this.apply {
            httpClient.sslSocketFactory(sslSocketFactory, x509TrustManager)
        }

        /**
         * TODO 添加Dns 过滤
         */
         fun dns(dns: Dns) = this.apply {
            httpClient.dns(dns)
        }


        /**
         * TODO 添加缓存处理
         */
        fun cache(cache: Cache) = this.apply {
            httpClient.cache(cache)
        }


        /**
         * 配置是否重定向
         */
        override fun setFollowRedirects(allowRedirect: Boolean) = this.apply {
            httpClient.followRedirects(allowRedirect)
        }

        fun setHead(hashMap: HashMap<String, String>) = this.apply {
            heads.clear()
            heads.putAll(hashMap)
        }

        fun build(): OkHttpClientBuilder {
            return instance
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
                Log.setEnable(true)
                Log.i("okhttpUrl", it.request().url().toString())
                it.proceed(it.request())
            }
            addInterceptor(HttpLoggingInterceptor().setLevel(logBody))
        } else {
            Log.setEnable(false)
            addInterceptor(HttpLoggingInterceptor().setLevel(logNone))
        }

        if (IS_NEED_COOKIE) httpClient.cookieJar(CookieCaches(CookieManager.instance))
        followRedirects(IS_REDIRECT_ALLOW)
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
            followSslRedirects(IS_REDIRECT_ALLOW)
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
        IS_REDIRECT_ALLOW = allowRedirect
    }

}