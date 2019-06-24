package com.stormkid.okhttpkt.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.os.Build
import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat


/**
建立
@author ke_li
@date 2018/7/18
 */
class NotificationUtil private constructor(private val context: Context) : ContextWrapper(context) {
    private val id = "yghys_channel"
    private val name = "channel_yghys"
    private val notifyId = 9527
    private var title = ""
    private var process = 0
    private var image = 0
    private var content = ""
    private var icon = 0
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            getManager().createNotificationChannel(channel)
        }
    }

    companion object {
        private val notifyContents = hashMapOf<Int, Notification>()
        fun getInstance(context: Context) = NotificationUtil(context)
    }

    fun setImage(@DrawableRes image: Int): NotificationUtil {
        this.image = image
        return this
    }

    fun setTitle(title: String): NotificationUtil {
        this.title = title
        return this
    }

    fun setIcon(@DrawableRes icon:Int): NotificationUtil {
        this.icon = icon
        return this
    }

    fun updateProgress(process: Int) {
        val notify = notifyContents[notifyId]?:return
        if (process == 0) getManager().cancel(notifyId)
        if (Build.VERSION.SDK_INT >= 24){
            val build = Notification.Builder.recoverBuilder(context,notify)
            build.setProgress(100,process,false)
            build.setContentText("$process %")
            getManager().notify(notifyId,notify)
        }else{
            notify.contentView.setProgressBar(android.R.id.progress,100,process,false)
            getManager().notify(notifyId,notify)
        }

    }

    fun setProgress(process: Int): NotificationUtil {
        this.process = process
        return this
    }

    fun setContent(content: String): NotificationUtil {
        this.content = content
        return this
    }


    fun sendNotification() {
        notifyContents.clear()
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel()
            val notification = getChannelNotification()?.build()
            notifyContents[notifyId] = notification?:return
        } else {
            val notification = getNotification_25().build()
            notifyContents[notifyId] = notification
        }


    }


    private fun getNotification_25(): NotificationCompat.Builder =
         NotificationCompat.Builder(applicationContext).apply {
            setContentTitle(title)
            if (TextUtils.isEmpty(content))
                setProgress(100, process, false)
            else {
                setContent(content)
                setAutoCancel(true)
            }
            val res = context.resources
            val bitmap = BitmapFactory.decodeResource(res,image)
            setSmallIcon(icon)
            setLargeIcon(bitmap)
            setAutoCancel(false)
        }


    private fun getChannelNotification(): Notification.Builder? {
        return if (Build.VERSION.SDK_INT >= 26)
            Notification.Builder(applicationContext, id)
                    .apply {
                        setContentTitle(title)
                        if (TextUtils.isEmpty(content))
                            setProgress(100, process, false)
                        else {
                            setContent(content)
                            setAutoCancel(true)
                        }
                        val res = context.resources
                        val bitmap = BitmapFactory.decodeResource(res,image)
                        setSmallIcon(icon)
                        setLargeIcon(bitmap)
                        setAutoCancel(false)
                    }
        else null
    }


    fun cancel() {
        getManager().cancel(notifyId)
    }

    private fun getManager(): NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

}