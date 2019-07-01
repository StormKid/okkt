@file:Suppress("DeferredResultUnused")

package com.stormkid.okhttpkt.asyc

import kotlinx.coroutines.*

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
              function.invoke()
          }
      }
    }

    fun runPost(function: () -> Unit){
        runBlocking {
            launch(Dispatchers.Unconfined){
                function.invoke()
            }
        }
    }
}