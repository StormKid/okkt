package com.stormkid.okhttpkt.core

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import com.google.gson.Gson
import com.stormkid.okhttpkt.asyc.DownloadCallback
import com.stormkid.okhttpkt.rule.CallbackRule
import com.stormkid.okhttpkt.rule.ClientRule
import com.stormkid.okhttpkt.rule.DownLoadRule
import com.stormkid.okhttpkt.rule.ProGressRule
import com.stormkid.okhttpkt.utils.CallbackNeed
import com.stormkid.okhttpkt.utils.FileCallbackNeed
import com.stormkid.okhttpkt.utils.FileResponseBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import java.io.File

/**
分别启动工厂模式创建更多okhttpclient或者启动单例模式的okhttpclient
@author ke_li
@time 2018/5/9
 */
class Okkt private constructor() {
    /**
     * 是否是工厂模式
     */
    private var isFactory = false
    /**
     * 默认的clientType为单例模式
     */
    private var clientType = SINGLE_CLIENT

    /**
     * 默认获取http对象
     */
    private var clientNetType = HTTP_TYPE

    /**
     * 默认为http请求单例对象
     */
    private var okHttpClient: OkHttpClient = OkHttpClientBuilder.Builder.build().getHttpClient().build()


    /**
     * 设置baseUrl
     */
    private var baseUrl = ""

    /**
     * 设置错误指令，默认不处理
     */
    private var error = "网络链接失效，请检查网络连接"

    companion object {
        /**
         * 获取单例对象
         */
        const val SINGLE_CLIENT = "SINGLE_CLIENT"
        /**
         * 获取工厂对象
         */
        const val FACTORY_CLIENT = "FACTORY_CLIENT"

        /**
         * 获取http请求的OkHttpclient对象
         */
        const val HTTP_TYPE = "HTTP"
        /**
         * 获取https请求的OkHttpclient对象
         */
        const val HTTPS_TYPE = "HTTPS"

        /**
         * 获取自定义OkHttpclient对象
         */
        const val COMMOM_TYPE = "COMMOM_TYPE"

        @JvmStatic
        val instance by lazy { Okkt() }


    }


    /**
     * 设置获取的okhttpclient
     */
    fun setClientType(type: String): Okkt {
        if (type == FACTORY_CLIENT || type == SINGLE_CLIENT)
            this.clientType = type
        else this.clientType = SINGLE_CLIENT
        return this
    }

    /**
     * 设置请求方式，http请求，https请求或者自定义全局方式
     */
    fun setNetClientType(type: String): Okkt {
        if (type == HTTPS_TYPE || type == HTTP_TYPE || type == COMMOM_TYPE)
            this.clientNetType = type
        else this.clientType = HTTP_TYPE
        return this
    }

    /**
     *  可调用不init采取默认调用
     */
    fun initHttpClient() {
        initNetType(OkHttpClientBuilder.Builder.build())
        when (clientType) {
            FACTORY_CLIENT -> isFactory = true
        }
    }

    /**
     * 获取相应的对象
     */
    private fun getHttpClient(): OkHttpClient {
        if (isFactory) initNetType(OkHttpClientBuilder.Builder.build())
        return okHttpClient
    }

    private fun getFactoryClient() = OkHttpClientBuilder.Builder.build().getHttpClient().build()

    /**
     * 更新头部布局
     */
    fun initHead(map: HashMap<String, String>): Okkt {
        initNetType(OkHttpClientBuilder.Builder.setHead(map).build())
        return this
    }


    private fun initNetType(clientRule: ClientRule) {

        when (clientNetType) {
            HTTP_TYPE -> okHttpClient = clientRule.getHttpClient().build()
            HTTPS_TYPE -> okHttpClient = clientRule.getHttpsClient().build()
            COMMOM_TYPE -> okHttpClient = clientRule.getCustomnClient().build()
        }
    }

    /**
     * 是否需要cookie
     */
    fun isNeedCookie(isNeed:Boolean): Okkt {
        OkHttpClientBuilder.Builder.build().isNeedCookie(isNeed)
        return this
    }


    /**
     * 全部以long为单位输入请求超时时间
     */
    fun setTimeOut(time: Long): Okkt {
        OkHttpClientBuilder.Builder.build().setTimeOut(time)
        return this
    }

    /**
     * 是否显示log
     */
    fun isLogShow(boolean: Boolean): Okkt {
        OkHttpClientBuilder.Builder.build().isLogShow(boolean)
        return this
    }


    /**
     * 设置主体url
     */
    fun setBase(url: String): Okkt {
        this.baseUrl = url
        return this
    }

    /**
     * 获取主体url
     */
    fun getBase() = this.baseUrl

    /**
     * 设置错误信息
     */
    fun setErr(err: String): Okkt {
        this.error = err
        return this
    }


    inner class Builder {
        private var url = ""
        private val params = hashMapOf<String, String>()
        private val body = hashMapOf<String, String>()
        private var file = File("")
        private var filePath = ""
        private var fileNameKey = "file"
        /**
         * 获取单独flag对象
         */
        private var flag = ""

        /**
         * 输入url
         */
        fun setUrl(url: String): Builder {
            this.url = baseUrl + url
            return this
        }

        /**
         * 输入的全部url
         */
        fun setFullUrl(url: String): Builder {
            this.url = url
            return this
        }


        /**
         * 获取独有的请求标识,多连接的时候进行回调处理
         */
        fun setFlag(flag: String): Builder {
            this.flag = flag
            return this
        }

        /**
         * 输入请求body
         */
        fun putBody(params: HashMap<String, String>): Builder {
            this.body.clear()
            this.body.putAll(params)
            return this
        }

        /**
         * 传入file
         */
        fun putFile(file: File): Builder {
            this.file = file
            return this
        }

        fun setFilePath(filePath: String): Builder {
            this.filePath = filePath
            return this
        }

        /**
         * 传入fileNameKey
         */
        fun putFileNameKey(key: String): Builder {
            this.fileNameKey = key
            return this
        }


        /**
         * 传入url拼接属性
         */
        fun setParams(params: HashMap<String, String>): Builder {
            this.params.clear()
            this.params.putAll(params)
            return this
        }

        private fun init(): Request.Builder {
            val url = url + initUrl(params)
            val request = Request.Builder().url(url)
            return request
        }

        ////////////////////////////////请求到 String ////////////////////////////////////////
        fun getString(call: CallbackRule<String>){
            val request = init().build()
            getHttpClient().newCall(request).enqueue(OkStringCallback(call, CallbackNeed(flag, error)))
        }

        fun  postString(call: CallbackRule<String>) {
            val request = init()
            val requestBody = FormBody.Builder().apply {
                body.forEach { this.add(it.key, it.value) }
            }.build()
            getHttpClient().newCall(request.post(requestBody).build()).enqueue(OkStringCallback(call, CallbackNeed(flag, error)))
        }

        fun  postStringJson(call: CallbackRule<String>) {
            val request = init()
            val json = Gson().toJson(body)
            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
            getHttpClient().newCall(request.post(requestBody).build()).enqueue(OkStringCallback(call, CallbackNeed(flag, error)))
        }

        fun postStringJson(json: String, call: CallbackRule<String>) {
            val request = init()
            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
            getHttpClient().newCall(request.post(requestBody).build()).enqueue(OkStringCallback(call, CallbackNeed(flag, error)))
        }

        ////////////////////////////////////////////普通请求///////////////////////////////////////////////////////

        fun <T> get(call: CallbackRule<T>) {
            val request = init().build()
            getHttpClient().newCall(request).enqueue(OkCallback(call, CallbackNeed(flag, error)))
        }

        fun <T> post(call: CallbackRule<T>) {
            val request = init()
            val requestBody = FormBody.Builder().apply {
                body.forEach { this.add(it.key, it.value) }
            }.build()
            getHttpClient().newCall(request.post(requestBody).build()).enqueue(OkCallback(call, CallbackNeed(flag, error)))
        }

        fun <T> postJson(call: CallbackRule<T>) {
            val request = init()
            val json = Gson().toJson(body)
            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
            getHttpClient().newCall(request.post(requestBody).build()).enqueue(OkCallback(call, CallbackNeed(flag, error)))
        }

        fun <T> postJson(json: String, call: CallbackRule<T>) {
            val request = init()
            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
            getHttpClient().newCall(request.post(requestBody).build()).enqueue(OkCallback(call, CallbackNeed(flag, error)))
        }

        /**
         * 直传文件
         */
        fun <T> postFile(call: CallbackRule<T>) {
            val request = init()
            if (file.exists()) {
                val multipartBody = initFileBody(body).build()
//                val resultBody = FileResuestBody(multipartBody,call)
                setTimeOut(60000)
                getFactoryClient().newCall(request.post(multipartBody).build()).enqueue(OkCallback(call, CallbackNeed(flag, error)))
            }
        }


        /**
         * 下载文件
         */
        fun downLoad(context: Context, proGressRule: ProGressRule) {
            val request = init()
            val fileCallbackNeed = FileCallbackNeed(filePath, context, 0)
            runBlocking { launch(Dispatchers.Main) { proGressRule.onStartRequest() } }
            setTimeOut(60000)
            getFactoryClient().newBuilder().addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                val body = FileResponseBody(response.body()!!, fileCallbackNeed, proGressRule)
                response.newBuilder().body(body).build()
            }.build().newCall(request.build()).enqueue(com.stormkid.okhttpkt.asyc.DownloadManager(fileCallbackNeed, proGressRule))
        }


        private fun initUrl(map: HashMap<String, String>) = "".let {
            var onFirst = false
            var result = ""
            map.forEach {
                if (onFirst) result += ("&${it.key}=${it.value}")
                else {
                    onFirst = true
                    result += ("?${it.key}=${it.value}")
                }
            }
            result
        }

        private fun initFileBody(map: HashMap<String, String>): MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            map.forEach {
                this.addFormDataPart(it.key, it.value)
            }
            this.addFormDataPart(fileNameKey, file.name, MultipartBody.create(MultipartBody.FORM, file))
        }

    }

    /**
     * 系统下载器下载文件
     */
    fun download(url: String, title: String, desc: String, context: Context,downLoadRule: DownLoadRule) = let {
        val uri = Uri.parse(url)
        val req = DownloadManager.Request(uri).apply {
            //设置WIFI下进行更新
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            //下载中和下载完后都显示通知栏
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, title)
            //通知栏标题
            setTitle(title)
            //通知栏描述信息
            setDescription(desc)
            //设置类型为.apk
            setMimeType("application/vnd.android.package-archive")

        }

        //获取下载任务ID
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        context.registerReceiver(DownloadCallback(downLoadRule), IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        try {
            dm.enqueue(req)
        }catch (exception:Exception){
            downLoadRule.onNetErr()
            -1L
        }

    }

    fun checkId(id: Long, context: Context) {
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        try {
            dm.remove(id)
        } catch (exception: Exception) {

        }
    }


}