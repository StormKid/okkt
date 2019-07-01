package com.stormkid.okhttpkt.rule

import okhttp3.Cache
import okhttp3.Dns
import okhttp3.Interceptor
import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

interface FactoryRule {

    fun addInterceptor(interceptor: Interceptor): FactoryRule

    fun addNetworkInterceptor(interceptor: Interceptor):FactoryRule

    fun writeTimeOut(time:Long):FactoryRule

    fun socketFactory(socketFactory: SocketFactory):FactoryRule

    fun SSLSocketFactory(sslSocketFactory: SSLSocketFactory,x509TrustManager: X509TrustManager):FactoryRule

    fun dns(dns: Dns):FactoryRule

    fun cache(cache: Cache):FactoryRule

    fun setTimeOut(time: Long)

    fun setCookie(isNeed: Boolean)
}