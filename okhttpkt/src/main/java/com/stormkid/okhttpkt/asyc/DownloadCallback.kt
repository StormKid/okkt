package com.stormkid.okhttpkt.asyc

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.stormkid.okhttpkt.core.OkTk
import com.stormkid.okhttpkt.rule.DownLoadRule


/**

@author ke_li
@date 2018/11/19
 */
class DownloadCallback(private val downLoadRule: DownLoadRule) : BroadcastReceiver() {

    companion object {
        val filter by lazy { IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE) }
    }

    private var ID = -1L

    fun initId(downId: Long) {
        this.ID = downId
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        ID = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0) ?: -1L
        if (ID != -1L) {
            val manager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uriForDownloadedFile = manager.getUriForDownloadedFile(ID)
            downLoadRule.onFinished(uriForDownloadedFile,this)
        } else {
            OkTk.instance.checkId(ID, context!!)
        }
    }
}