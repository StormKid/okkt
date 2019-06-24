package com.stormkid.okhttpkt.utils

import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
GSON 工具
@author ke_li
@date 2018/5/28
 */
object GsonFactory{
    private val gson = GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    fun <T> formart(json: String, clazz: Class<out T>): T{
        return gson.fromJson<T>(json, clazz)
    }

    fun <T> formart(json: String, type: Type): T {
        return gson.fromJson<T>(json, type)
    }

    fun toJson(body:Any)=let {
        gson.toJson(body)
    }
}