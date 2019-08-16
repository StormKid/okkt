package com.stormkid.okhttpdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stormkid.okhttpkt.core.OkWebsocket
import com.stormkid.okhttpkt.rule.StringCallback
import com.stormkid.okhttpkt.rule.WebsocketCallbackRule
import com.stormkid.okhttpkt.utils.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),StringCallback {
    override suspend fun onSuccess(entity: String, flag: String) {
    }

    override suspend fun onFailed(error: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
//        GsonFactory.reBuild()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        GsonFactory.setDateFormat()
//        GsonFactory.setPrinting()
//        Okkt.instance.Builder().setUrl("/part").get(this)
//        Okkt.instance.Builder().setUrl("/part").setParams(hashMapOf("id" to "what")).postJson(object:
//            CallbackRule<String> {
//            override suspend fun onSuccess(entity: String, flag: String) {
//            }
//
//            override suspend fun onFailed(error: String) {
//            }
//
//        })
//
//        Okkt.instance.Builder().setUrl("/part").putFileNameKey("file").putFile(externalCacheDir!!).postFile(object :CallbackRule<Any>{
//            override suspend fun onSuccess(entity: Any, flag: String) {
//            }
//
//            override suspend fun onFailed(error: String) {
//            }
//
//        } )
//
//        Okkt.instance.Builder().setFilePath("path").setFullUrl("http://xxxxxx").downLoad(applicationContext,object :
//            ProGressRule {
//            override suspend fun getProgress(progress: Int) {
//            }
//
//            override suspend fun onFinished() {
//            }
//
//            override suspend fun onStartRequest() {
//            }
//
//            override suspend fun onOpenFile(file: File) {
//            }
//
//        })

//        Okkt.instance.TestBuilder().setUrl("http://www.baidu.com").testGet(object : TestCallbackRule{
//            override suspend fun onResponse(response: TestCallbackRule.Response) {
//                Log.w("response","${response.body.toString()}----${response.heads}")
//            }
//
//            override suspend fun onErr(err: String) {
//            }
//
//        })

        click.setOnClickListener {
            OkWebsocket.instance.Config().setTimeOut(150000).showLog(true)
            OkWebsocket.instance.Builder().setUrl("http://10.0.0.2:8100").build().startSocket(object : WebsocketCallbackRule<String>{
                override suspend fun onMessageSuccess(massage: String) {
                    Log.w(massage)
                    click.text = massage
                }

                override suspend fun onSocketDrop(err: String) {
                }

                override suspend fun onClosed() {
                }

            })
        }
    }
}
