package com.stormkid.okhttpkt.rule

import okhttp3.OkHttpClient

/**
@author ke_li
@date 2018/5/23
 */
interface ClientRule {
    fun getHttpClient():OkHttpClient.Builder
    fun getHttpsClient():OkHttpClient.Builder
    fun getCustomnClient():OkHttpClient.Builder
    fun setTimeOut(time:Long)
    fun isLogShow(boolean: Boolean)
    fun isNeedCookie(isNeed:Boolean)
}