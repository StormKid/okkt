package com.stormkid.okhttpkt.utils

import android.util.Log
import com.stormkid.okhttpkt.rule.ProGressRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


/**

@author ke_li
@date 2018/7/20
 */
class FileResponseBody(private val responseBody: ResponseBody,private val fileCallbackNeed:FileCallbackNeed,
                          private val proGressRule: ProGressRule) : ResponseBody() {
    override fun contentLength(): Long= responseBody.contentLength()

    override fun contentType(): MediaType? = responseBody.contentType()


    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0   //不断统计当前下载好的数据
                //接口回调后写入文件
                if (bytesRead==-1L){
                    val body = sink.inputStream()
                    writeFile(body)
                }
                val current = caculateProgress(totalBytesRead,responseBody.contentLength())
                runBlocking { launch (Dispatchers.Main){
                    proGressRule.getProgress(current) } }
                return bytesRead
            }
        }

    }

    private fun caculateProgress(current: Long, total:Long) = let {
        val percent = current * 100 / total
        percent.toInt()

    }
    override fun source(): BufferedSource = Okio.buffer(source(responseBody.source()))

    /**
     * 直传下载
     */
    private fun writeFile(inputStream: InputStream) {
        try {
            val byte = ByteArray(2048)
            val path = fileCallbackNeed.context.externalCacheDir?.absolutePath + "/" + fileCallbackNeed.selfPath
            val fileOutputStream = FileOutputStream(path)
            var len: Int
            var current = 0L
            runBlocking {
                while (true) {
                    len = inputStream.read(byte)
                    if (len == -1) {
                        inputStream.close()
                        fileOutputStream.close()
                        launch(Dispatchers.Main) { proGressRule.onOpenFile(File(path)) }
                        return@runBlocking
                    }
                    fileOutputStream.write(byte, 0, len)
                    current += len
                }
            }
        }catch (e:Exception){
            Log.w("okktx","Your file permission or file path need to look out or otherwise")
        }

    }
}