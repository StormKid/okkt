package com.stormkid.okhttpkt.cache

import android.content.Context
import java.io.File


/**
 * 使用此api 必须要申请文件读写权限
获取文件的缓存
@author ke_li
@date 2018/7/18
 */
object FileCache {

    /**
     * 写入文件的缓存
     */
    fun saveCache(context: Context, file: File) {
        if (context.externalCacheDir != null && !context.externalCacheDir!!.exists()) {
            context.externalCacheDir!!.mkdir()
        }
        val path = "${context.externalCacheDir!!.absolutePath}/ ${file.name}"
        val cacheFile = File(path)
        file.copyTo(cacheFile, true)
    }


    /**
     * 获取缓存文件
     */
    fun getCache(context: Context, name: String): File? = let {
        if (context.externalCacheDir != null && !context.externalCacheDir!!.exists()) {
            null
        } else {
            val path = "${context.externalCacheDir!!.absolutePath}/ $name"
            try {
                File(path)
            } catch (e: Exception) {
                null
            }
        }
    }
}