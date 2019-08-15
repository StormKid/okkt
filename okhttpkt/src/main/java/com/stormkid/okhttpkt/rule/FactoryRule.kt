package com.stormkid.okhttpkt.rule

import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

interface FactoryRule {

//    fun addInterceptor(interceptor: Interceptor):FactoryRule

//    fun addNetworkInterceptor(interceptor: Interceptor):FactoryRule

    fun writeTimeOut(time:Long):FactoryRule

    fun socketFactory(socketFactory: SocketFactory):FactoryRule

    fun SSLSocketFactory(sslSocketFactory: SSLSocketFactory,x509TrustManager: X509TrustManager):FactoryRule

//    fun dns(dns: Dns):FactoryRule

//    fun cache(cache: Cache):FactoryRule

    fun setTimeOut(time: Long): FactoryRule

    fun setCookie(isNeed: Boolean): FactoryRule

    fun setFollowRedirects(allowRedirect: Boolean):FactoryRule

    fun cleanInterceptor():FactoryRule
}