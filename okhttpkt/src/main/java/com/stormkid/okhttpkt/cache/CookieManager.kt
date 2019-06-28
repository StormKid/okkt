package com.stormkid.okhttpkt.cache

import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

/**
操作cookie类
@author ke_li
@date 2019/6/28
 */
class CookieManager private constructor() : CookieRule {

    private val COOKIE_HOST_KEY = "COOKIE_HOST_KEY"
    private val COOKIE_NAME_KEY = "COOKIE_NAME_KEY"

    private val cookies: HashMap<String, ConcurrentHashMap<String, Cookie>> = hashMapOf()

    companion object {
        val instance by lazy { CookieManager() }
    }

    override fun add(httpUrl: HttpUrl, cookie: Cookie) {
        if (!cookie.persistent()) return
        val hostKey = doHost(httpUrl)
        val nameKey = doName(cookie)?:return
        if (!cookies.containsKey(hostKey)){
            cookies[hostKey] = ConcurrentHashMap()
        }
        cookies[hostKey]?.set(nameKey, cookie)
    }

    override fun add(httpUrl: HttpUrl, cookies: MutableList<Cookie>) {
        cookies.forEach {
            if (!isCookieTimeout(it))
                add(httpUrl,it)
        }
    }

    override fun get(httpUrl: HttpUrl): MutableList<Cookie> {
    }

    override fun getCookies(): MutableList<Cookie> {
    }

    override fun remove(httpUrl: HttpUrl, cookie: Cookie): Boolean {

    }

    override fun removeAll(): Boolean {
        cookies.clear()
        return true
    }

    fun isCookieTimeout(cookie: Cookie): Boolean {
        return cookie.expiresAt() < System.currentTimeMillis()
    }


    private fun getCookies(hostKey:String){
    }

    private fun removeCookie(cookie: Cookie){
        val name = doName(cookie)
        cookies.forEach {

        }
    }


    private fun doHost(httpUrl: HttpUrl) =
        if (httpUrl.host().startsWith(COOKIE_HOST_KEY)) httpUrl.host()
        else COOKIE_HOST_KEY + httpUrl.host()


    private fun doName(cookie: Cookie?)=
        if (cookie==null) null
        else cookie.name() + cookie.domain()
}