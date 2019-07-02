package com.stormkid.okhttpkt.cache

import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.concurrent.ConcurrentHashMap

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
        val nameKey = doName(cookie) ?: return
        if (!cookies.containsKey(hostKey)) {
            cookies[hostKey] = ConcurrentHashMap()
        }
        cookies[hostKey]?.set(nameKey, cookie)
    }

    override fun add(httpUrl: HttpUrl, cookies: MutableList<Cookie>) {
        cookies.forEach {
            if (!isCookieTimeout(it))
                add(httpUrl, it)
        }
    }

    override fun get(httpUrl: HttpUrl): MutableList<Cookie> {
        return getCookies(doHost(httpUrl))
    }

    override fun getCookies(): MutableList<Cookie> {
        val result = arrayListOf<Cookie>()
        cookies.keys.forEach {
            result.addAll(getCookies(it))
        }
        return result
    }

    override fun remove(httpUrl: HttpUrl, cookie: Cookie): Boolean {
        return removeCookie(doHost(httpUrl),cookie)
    }

    override fun removeAll(): Boolean {
        cookies.clear()
        return true
    }

    fun isCookieTimeout(cookie: Cookie): Boolean {
        return cookie.expiresAt() < System.currentTimeMillis()
    }

    /**
     * 返回cookie
     */
    fun getCookie(name: String): Cookie? {
        val cookies = getCookies()
        cookies.forEach {
            if (it.name() == name) return it
        }
        return null
    }

    private fun getCookies(hostKey: String): ArrayList<Cookie> {
        val result = arrayListOf<Cookie>()
        if (cookies.containsKey(hostKey)) {
            val currentCookie = this.cookies[hostKey]?.values
            currentCookie?.forEach {
                if (isCookieTimeout(it)){
                    removeCookie(hostKey,it)
                }else
                    result.add(it)
            }
        }
        return result
    }

    private fun removeCookie(hostKey: String, cookie: Cookie) = let {
        val name = doName(cookie)
        val back =
            try {
                if (this.cookies.containsKey(hostKey) && this.cookies[hostKey]!!.containsKey(name!!)) {
                    // 从内存中移除httpUrl对应的cookie
                    this.cookies[hostKey]?.remove(name)
                    true
                } else false
            } catch (e: Exception) {
                false
            }
        back
    }


    private fun doHost(httpUrl: HttpUrl) =
        if (httpUrl.host().startsWith(COOKIE_HOST_KEY)) httpUrl.host()
        else COOKIE_HOST_KEY + httpUrl.host()


    private fun doName(cookie: Cookie?) =
        if (cookie == null) null
        else cookie.name() + cookie.domain()


}