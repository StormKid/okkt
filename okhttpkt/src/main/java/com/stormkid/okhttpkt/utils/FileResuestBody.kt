package com.stormkid.okhttpkt.utils

import com.stormkid.okhttpkt.rule.ProGressRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException


class FileResuestBody (private val requestBody: RequestBody,private val proGressRule: ProGressRule): RequestBody(){
    private var bufferedSink:BufferedSink?=null
    override fun contentType(): MediaType? = requestBody.contentType()

    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null){
            bufferedSink = Okio.buffer(sink)
        }
        requestBody.writeTo(bufferedSink!!)
        bufferedSink?.flush()
    }

    override fun contentLength(): Long {
        return requestBody.contentLength()

    }

    /**
     * 写入，回调进度接口
     * @param sink Sink
     * @return Sink
     */
    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
             var bytesWritten = 0L
            //总字节长度，避免多次调用contentLength()方法
             var contentLength = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                //回调
                val progress = Math.round(bytesWritten/contentLength *100f)
               runBlocking { launch (Dispatchers.Main){  proGressRule.getProgress(progress) } }
            }
        }
    }
}