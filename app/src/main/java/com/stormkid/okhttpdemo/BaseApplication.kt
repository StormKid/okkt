package com.stormkid.okhttpdemo

import android.app.Application
import com.stormkid.okhttpkt.core.Okkt

/**

@author ke_li
@date 2019/7/1
 */
class BaseApplication:Application (){
    override fun onCreate() {
        super.onCreate()
        Okkt.instance.setBase("http://xxxx.com").setClientType(Okkt.FACTORY_CLIENT).isLogShow(true).setErr("xxxx").setNetClientType(Okkt.HTTPS_TYPE).setTimeOut(1000L).isNeedCookie(false).initHttpClient()
    }
}