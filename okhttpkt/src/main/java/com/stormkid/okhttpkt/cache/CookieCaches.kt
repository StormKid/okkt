package com.stormkid.okhttpkt.cache

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 存储cookie
@author ke_li
@date 2019/6/28
 */
class CookieCaches (private val cookieManager: CookieManager) : CookieJar{

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
       return cookieManager.get(url)
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieManager.add(url,cookies)
    }

}