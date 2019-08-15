package com.stormkid.okhttpkt.rule

/**
websocket 回调
@author ke_li
@date 2019/8/15
 */
interface WebsocketCallbackRule <in T>{
    suspend fun onMessageSuccess(massage: T)

    suspend fun onSocketDrop(err: String)

    suspend fun onClosed()
}