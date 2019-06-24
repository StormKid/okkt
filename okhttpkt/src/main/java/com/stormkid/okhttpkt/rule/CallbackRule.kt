package com.stormkid.okhttpkt.rule

/**
 * 网络框架回调约束
@author ke_li
@date 2018/5/24
 */
interface CallbackRule <in T>{
    /**
     * 成功请求
     * @entity 请求返回的json实体
     * @flag 同一个页面设置不同请求
     */
   suspend fun  onSuccess(entity:T,flag:String)

    /**
     * 请求失败
     */
    suspend fun onFailed(error:String)
}