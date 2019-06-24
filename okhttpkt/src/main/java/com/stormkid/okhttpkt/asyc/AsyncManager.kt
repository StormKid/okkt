package com.stormkid.okhttpkt.asyc

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 *
 * 异步线程
@author ke_li
@date 2018/7/17
 */
object AsyncManager {

    fun runLazy(function:()->Unit){
      runBlocking {
          async(start = CoroutineStart.LAZY) {
              function
          }
      }
    }
}