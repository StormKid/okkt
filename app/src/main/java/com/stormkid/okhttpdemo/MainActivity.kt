package com.stormkid.okhttpdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import com.stormkid.okhttpkt.rule.ProGressRule
import com.stormkid.okhttpkt.rule.StringCallback
import com.stormkid.okhttpkt.utils.GsonFactory
import java.io.File

class MainActivity : AppCompatActivity(),StringCallback {
    override suspend fun onSuccess(entity: String, flag: String) {
    }

    override suspend fun onFailed(error: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
        GsonFactory.reBuild()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GsonFactory.setDateFormat()
        GsonFactory.setPrinting()
        Okkt.instance.Builder().setUrl("/part").get(this)
        Okkt.instance.Builder().setUrl("/part").setParams(hashMapOf("id" to "what")).postJson(object:CallbackRule<String>{
            override suspend fun onSuccess(entity: String, flag: String) {
            }

            override suspend fun onFailed(error: String) {
            }

        })

        Okkt.instance.Builder().setUrl("/part").putFileNameKey("file").putFile(externalCacheDir!!).postFile(object :CallbackRule<Any>{
            override suspend fun onSuccess(entity: Any, flag: String) {
            }

            override suspend fun onFailed(error: String) {
            }

        } )

        Okkt.instance.Builder().setFilePath("path").setFullUrl("http://xxxxxx").downLoad(applicationContext,object : ProGressRule{
            override suspend fun getProgress(progress: Int) {
            }

            override suspend fun onFinished() {
            }

            override suspend fun onStartRequest() {
            }

            override suspend fun onOpenFile(file: File) {
            }

        })
    }
}
