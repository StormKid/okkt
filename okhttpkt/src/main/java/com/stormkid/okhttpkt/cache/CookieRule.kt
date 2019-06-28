package com.stormkid.okhttpkt.cache

import okhttp3.Cookie
import okhttp3.HttpUrl

/**
规范代码
@author ke_li
@date 2019/6/28
 */
interface CookieRule {
    /**  添加cookie */
    fun add(httpUrl: HttpUrl, cookie: Cookie)

    /** 添加指定httpurl cookie集合 */
    fun add(httpUrl: HttpUrl, cookies: MutableList<Cookie>)

    /** 根据HttpUrl从缓存中读取cookie集合 */
    fun get(httpUrl: HttpUrl):MutableList<Cookie>

    /** 获取全部缓存cookie */
    fun getCookies(): MutableList<Cookie>

    /**  移除指定httpurl cookie集合 */
    fun remove(httpUrl: HttpUrl, cookie: Cookie):Boolean

    /** 移除所有cookie */
    fun removeAll():Boolean

}