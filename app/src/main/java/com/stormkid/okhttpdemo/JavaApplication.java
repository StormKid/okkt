package com.stormkid.okhttpdemo;

import android.app.Application;

import com.stormkid.okhttpkt.java.Okkt4j;

/**
 * @author ke_li
 * @date 2019/9/9
 */
public class JavaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Okkt4j.getInstance().setBase("https://api.isoyu.com/api/").setNetClientType(Okkt4j.HTTPS_TYPE).isLogShow(true).setTimeOut(10000L).initHttpClient();
    }
}
