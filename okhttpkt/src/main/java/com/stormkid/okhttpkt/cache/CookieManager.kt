package com.stormkid.okhttpkt.cache

import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 操作cookie类
@author ke_li
@date 2019/6/28
 */
class CookieManager: CookieRule {
    override fun add(httpUrl: HttpUrl, cookie: Cookie) {
    }

    override fun add(httpUrl: HttpUrl, cookies: MutableList<Cookie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(httpUrl: HttpUrl) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCookies() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(httpUrl: HttpUrl, cookie: Cookie) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}