package com.stormkid.okhttpkt.rule

import android.content.BroadcastReceiver
import android.net.Uri

/**

@author ke_li
@date 2018/11/19
 */
interface DownLoadRule {
    fun onFinished(uri: Uri?,broadcastReceiver: BroadcastReceiver)

    fun onNetErr()
}