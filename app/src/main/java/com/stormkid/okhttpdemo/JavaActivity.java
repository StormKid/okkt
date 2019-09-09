package com.stormkid.okhttpdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stormkid.okhttpdemo.entities.TestEntity;
import com.stormkid.okhttpkt.java.CallbackRule;
import com.stormkid.okhttpkt.java.Okkt4j;
import com.stormkid.okhttpkt.utils.Log;

import java.util.HashMap;

/**
 * @author ke_li
 * @date 2019/9/9
 */
public class JavaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "广东省_深圳市");
        map.put("page", "0");
        Okkt4j.getInstance().getBuilder().setUrl("News/local_news").setParams(map).get(new CallbackRule<TestEntity>() {

            @Override
            public void onSuccess(TestEntity testEntity, String flag) {
                Log.INSTANCE.d(testEntity.getMsg());
            }

            @Override
             public void onFailed(String error) {
                 Log.INSTANCE.d(error);
             }
         });
    }




}
