package com.stormkid.okhttpkt.cache

import android.content.Context
import android.os.Environment
import java.io.File


/**
获取文件的缓存
@author ke_li
@date 2018/7/18
 */
object FileCache {

    /**
     * 写入文件的缓存
     */
    fun saveCache(context: Context,file:File){
        val path = getDiskCachePath(context)

    }


    /**
     * 获取缓存文件
     */
    fun getCache(){}


    fun getDiskCachePath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            context.externalCacheDir!!.path
        } else {
            context.cacheDir.path
        }
    }

    fun getExternalPath(context: Context) = context.packageResourcePath
}