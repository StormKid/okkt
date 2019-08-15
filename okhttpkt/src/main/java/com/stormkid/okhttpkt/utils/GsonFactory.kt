package com.stormkid.okhttpkt.utils

import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
GSON 工具
@author ke_li
@date 2018/5/28
 */
object GsonFactory{

    private var builder = GsonBuilder()

    fun setPrinting(){
        builder.setPrettyPrinting()
    }

    fun setDateFormat(vararg format:String){
        if (format.isEmpty()) builder.setDateFormat("yyyy-MM-dd HH:mm:ss")
        else builder.setDateFormat(format[0])
    }

    fun reBuild(){
        builder = GsonBuilder()
    }

    private val gson = builder.create()


    fun <T> formart(json: String, type: Type): T {
        return gson.fromJson(json, type)
    }

    fun toJson(body:Any)=let {
        gson.toJson(body)
    }
}