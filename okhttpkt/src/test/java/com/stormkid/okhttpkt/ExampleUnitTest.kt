package com.stormkid.okhttpkt

import com.stormkid.okhttpkt.core.OkTk
import com.stormkid.okhttpkt.rule.CallbackRule
import com.stormkid.okhttpkt.rule.EntityRule
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.reflect.ParameterizedType

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun take(){
        OkTk.instance.setClientType(OkTk.FACTORY_CLIENT).initHttpClient()
        for (i in 1..4){
          Runnable {
              println("当前是${i}线程名为${Thread.currentThread().name}---所对应对象"+  OkTk.instance)
          }.run()
        }
    }

    @Test
    fun test(){
        val list = arrayListOf<String>()
        val type = list.javaClass.genericSuperclass as ParameterizedType
         val name = type.actualTypeArguments[0]
        System.out.print(name)
    }
}
