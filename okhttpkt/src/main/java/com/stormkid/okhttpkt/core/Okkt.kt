package com.stormkid.okhttpkt.core

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import com.google.gson.Gson
import com.stormkid.okhttpkt.asyc.DownloadCallback
import com.stormkid.okhttpkt.rule.*
import com.stormkid.okhttpkt.utils.CallbackNeed
import com.stormkid.okhttpkt.utils.FileCallbackNeed
import com.stormkid.okhttpkt.utils.FileResponseBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
    fun isNeedCookie(isNeed: Boolean): Okkt {
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
     * 是否需要重定向
     */
    fun isAllowRedirect(isNeed: Boolean): Okkt {
        OkHttpClientBuilder.Builder.build().setFollowRedirects(isNeed)
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
    inner class TestBuilder {
        private val data = BuildData()

        fun setUrl(url: String): TestBuilder {
            data.url = url
            return this
        }

        /**
         * 传入url拼接属性
         */
        fun setParams(params: HashMap<String, String>): TestBuilder {
            data.params.clear()
            data.params.putAll(params)
            return this
        }


        fun putBody(params: HashMap<String, String>): TestBuilder {
            data.body.clear()
            data.body.putAll(params)
            return this
        }


        fun testGet(call: TestCallbackRule) {
            requestInit(data, GET_TYPE)?.enqueue(TestCallback(call))
        }

        fun testPost(call: TestCallbackRule) {
            requestInit(data, POST_FORM_TYPE)?.enqueue(TestCallback(call))
        }

        fun testPostJson(call: TestCallbackRule) {
            requestInit(data, POST_JSON_TYPE)?.enqueue(TestCallback(call))
        }

        fun testPostJson(json: String, call: TestCallbackRule) {
            data.json = json
            requestInit(data, POST_JSON_TYPE)?.enqueue(TestCallback(call))
        }


    }
    inner class Builder {
        private val data = BuildData()

        /**
         * 输入url
         */
        fun setUrl(url: String): Builder {
            data.url = baseUrl + url
            return this
        }

        /**
         * 输入的全部url
         */
        fun setFullUrl(url: String): Builder {
            data.url = url
            return this
        }


        /**
         * 获取独有的请求标识,多连接的时候进行回调处理
         */
        fun setFlag(flag: String): Builder {
            data.flag = flag
            return this
        }

        /**
         * 输入请求body
         */
        fun putBody(params: HashMap<String, String>): Builder {
            data.body.clear()
            data.body.putAll(params)
            return this
        }

        /**
         * 传入file
         */
        fun putFile(file: File): Builder {
            data.file = file
            return this
        }

        fun setFilePath(filePath: String): Builder {
            data.filePath = filePath
            return this
        }

        /**
         * 传入fileNameKey
         */
        fun putFileNameKey(key: String): Builder {
            data.fileNameKey = key
            return this
        }


        /**
         * 传入url拼接属性
         */
        fun setParams(params: HashMap<String, String>): Builder {
            data.params.clear()
            data.params.putAll(params)
            return this
        }

        ////////////////////////////////请求到 String ////////////////////////////////////////
        fun getString(call: StringCallback) {
            requestInit(data, GET_TYPE)?.enqueue(OkStringCallback(call, CallbackNeed(data.flag, error)))
        }

        fun postString(call: StringCallback) {
            requestInit(data, POST_FORM_TYPE)?.enqueue(OkStringCallback(call, CallbackNeed(data.flag, error)))
        }

        fun postStringJson(call: StringCallback) {
            requestInit(data, POST_JSON_TYPE)?.enqueue(OkStringCallback(call, CallbackNeed(data.flag, error)))
        }

        fun postStringJson(json: String, call: StringCallback) {
            data.json = json
            requestInit(data, POST_JSON_TYPE)?.enqueue(OkStringCallback(call, CallbackNeed(data.flag, error)))
        }

        ////////////////////////////////////////////普通请求///////////////////////////////////////////////////////

        fun <T> get(call: CallbackRule<T>) {
            requestInit(data, GET_TYPE)?.enqueue(OkCallback(call, CallbackNeed(data.flag, error)))
        }

        fun <T> post(call: CallbackRule<T>) {
            requestInit(data, POST_FORM_TYPE)?.enqueue(OkCallback(call, CallbackNeed(data.flag, error)))
        }

        fun <T> postJson(call: CallbackRule<T>) {
            requestInit(data, POST_JSON_TYPE)?.enqueue(OkCallback(call, CallbackNeed(data.flag, error)))
        }

        fun <T> postJson(json: String, call: CallbackRule<T>) {
            data.json = json
            requestInit(data, POST_JSON_TYPE)?.enqueue(OkCallback(call, CallbackNeed(data.flag, error)))
        }

        /**
         * 直传文件
         */
        fun <T> postFile(call: CallbackRule<T>) {
            requestInit(data, FILE_UPLOAD)?.enqueue(OkCallback(call, CallbackNeed(data.flag, error)))

        }


        /**
         * 下载文件
         */
        fun downLoad(context: Context, proGressRule: ProGressRule) {
            val url = data.url + initUrl(data.params)
            val request = Request.Builder().url(url)
            val fileCallbackNeed = FileCallbackNeed(data.filePath, context, 0)
            runBlocking { launch(Dispatchers.Main) { proGressRule.onStartRequest() } }
            setTimeOut(60000)
            getFactoryClient().newBuilder().addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                val body = FileResponseBody(response.body!!, fileCallbackNeed, proGressRule)
                response.newBuilder().body(body).build()
            }.build().newCall(request.build())
                .enqueue(com.stormkid.okhttpkt.asyc.DownloadManager(fileCallbackNeed, proGressRule))
        }

    }



    private val GET_TYPE = "GET_TYPE"
    private val POST_FORM_TYPE = "POST_TYPE"
    private val POST_JSON_TYPE = "POST_JSON_TYPE"
    private val FILE_UPLOAD = "FILE_UPLOAD"

    private fun requestInit(data: BuildData, type: String): Call? {
        val url = data.url + initUrl(data.params)
        val builder = Request.Builder().url(url)
        return when (type) {
            GET_TYPE -> {
                val request = builder.build()
                getHttpClient().newCall(request)
            }
            POST_FORM_TYPE -> {
                val requestBody = FormBody.Builder().apply {
                    data.body.forEach { this.add(it.key, it.value) }
                }.build()
                getHttpClient().newCall(builder.post(requestBody).build())
            }
            POST_JSON_TYPE -> {
                val json = if (TextUtils.isEmpty(data.json)) Gson().toJson(data.body) else data.json
                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                getHttpClient().newCall(builder.post(requestBody).build())
            }

            FILE_UPLOAD->{
                if (data.file.exists()) {
                    val body =   MultipartBody.Builder().setType(MultipartBody.FORM).apply {
                        data.body.forEach { addFormDataPart(it.key, it.value) }
                        addFormDataPart(data.fileNameKey, data.file.name, data.file.asRequestBody(MultipartBody.FORM))
                    }
                    val multipartBody = body.build()
                    setTimeOut(60000)
                    getFactoryClient().newCall(builder.post(multipartBody).build())
                }else null
            }
            else -> null
        }
    }

    /**
     * 系统下载器下载文件
     */
    fun download(url: String, title: String, desc: String, context: Context, downLoadRule: DownLoadRule) = let {
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
        } catch (exception: Exception) {
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


    /**
     *  请求必初始化的data
     */
    private data class BuildData(
        var body: HashMap<String, String> = hashMapOf(),
        var params: HashMap<String, String> = hashMapOf(),
        var url: String = "",
        var json: String = "",
        var file: File = File(""),
        var filePath: String = "",
        var fileNameKey: String = "file",
        var flag: String = ""
    )


}