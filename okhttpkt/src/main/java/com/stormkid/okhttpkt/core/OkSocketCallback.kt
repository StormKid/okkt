package com.stormkid.okhttpkt.core

import com.stormkid.okhttpkt.rule.WebsocketCallbackRule
import com.stormkid.okhttpkt.utils.GsonFactory
import com.stormkid.okhttpkt.utils.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.lang.reflect.ParameterizedType

/**

@author ke_li
@date 2019/8/15
 */
class OkSocketCallback<T>(private val callbackRule: WebsocketCallbackRule<T>) : WebSocketListener(){
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.cancel()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        try {
            val interfacesTypes = callbackRule.javaClass.genericInterfaces[0]
            val resultType = (interfacesTypes as ParameterizedType).actualTypeArguments
            val result = GsonFactory.formart<T>(text, resultType[0])
            runBlocking {  launch(Dispatchers.Main){ callbackRule.onMessageSuccess(result)}}
        }catch (e:Exception){
            Log.e("typeEER",e.message?:"")
            runBlocking{  launch(Dispatchers.Main){ callbackRule.onSocketDrop("数据服务异常，请联系管理员") }  }
            webSocket.cancel()
            return
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)

    }
}