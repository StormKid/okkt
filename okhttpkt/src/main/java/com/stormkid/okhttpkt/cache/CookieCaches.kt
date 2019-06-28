package com.stormkid.okhttpkt.cache

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 存储cookie
@author ke_li
@date 2019/6/28
 */
class CookieCaches : CookieJar{
    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}