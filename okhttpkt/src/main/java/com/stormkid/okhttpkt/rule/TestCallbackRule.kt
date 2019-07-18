package com.stormkid.okhttpkt.rule

/**

@author ke_li
@date 2019/7/2
 */
interface TestCallbackRule {
    suspend fun onResponse(response: Response)
    suspend fun onErr(err:String)

    data class Response(
        val body:String,
        val heads:HashMap<String,String>
    )
}