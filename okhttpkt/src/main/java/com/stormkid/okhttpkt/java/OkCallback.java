package com.stormkid.okhttpkt.java;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.stormkid.okhttpkt.utils.CallbackNeed;
import com.stormkid.okhttpkt.utils.GsonFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author ke_li
 * @date 2019/9/9
 */
public class OkCallback<T> implements Callback {

    private CallbackRule<T> callbackRule;
    private CallbackNeed need;
    private Handler handler = new Handler(Looper.getMainLooper());

    public OkCallback(CallbackRule<T> callbackRule, CallbackNeed need) {
        this.callbackRule = callbackRule;
        this.need = need;
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (null != response) {
            if (response.isSuccessful()) {
                if (null == response.body()) {
                    putMainThread(false, null, need.getErr_msg());
                } else {
                    String body = response.body().string();
                    //获取接口上的泛型注入
                    try {
                        Type[] genericInterfaces = callbackRule.getClass().getGenericInterfaces();
                        ParameterizedType interfacesTypes = (ParameterizedType)genericInterfaces[0];
                        Type[] resultType = interfacesTypes.getActualTypeArguments();
                        final T result = GsonFactory.INSTANCE.formart(body, resultType[0]);
                        putMainThread(true, result, need.getFlag());
                    }catch (Exception e){
                        Log.e("typeEER",e.getMessage());
                        putMainThread(false, null, need.getErr_msg());
                    }
                }
            } else
            putMainThread(false, null, response.message());
        } else    putMainThread(false, null, need.getErr_msg());
        call.cancel();
    }


    private void  putMainThread(final boolean isSuccess, final T body , final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) callbackRule.onSuccess(body,msg);
                else callbackRule.onFailed(msg);
            }
        });
    }
}
