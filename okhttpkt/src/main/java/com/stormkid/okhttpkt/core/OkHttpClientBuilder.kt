package com.stormkid.okhttpkt.core

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

    companion object {
        private val httpClient: OkHttpClient.Builder by lazy { OkHttpClient.Builder() }
        private val heads = hashMapOf<String, String>()
        private var showLog = false
        private val instance: OkHttpClientBuilder by lazy { OkHttpClientBuilder() }
    }

    object Builder : FactoryRule {
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
        this.interceptors().clear()
        if (heads.size != 0) this.addInterceptor {
            val myhead = Headers.of(heads)
            val builder = it.request().newBuilder()
            it.proceed(builder.headers(myhead).build())
        }

        if (showLog) {
            this.addInterceptor {
                android.util.Log.i("okhttpUrl", it.request().url().toString())
                it.proceed(it.request())
            }
            this.addInterceptor(HttpLoggingInterceptor().setLevel(logBody))
        } else this.addInterceptor(HttpLoggingInterceptor().setLevel(logNone))

        this.connectTimeout(ERR_TIME, TimeUnit.MILLISECONDS)
        this.readTimeout(ERR_TIME, TimeUnit.MILLISECONDS)
        this.writeTimeout(ERR_TIME, TimeUnit.MILLISECONDS)
    }

    /**
     * 获取默认httpsclient
     */
    override fun getHttpsClient() = httpClient.apply {
        getHttpClient().apply {
            val ssl = HttpsUtils.SSLParams()
            this.sslSocketFactory(ssl.sSLSocketFactory, ssl.trustManager)
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

}