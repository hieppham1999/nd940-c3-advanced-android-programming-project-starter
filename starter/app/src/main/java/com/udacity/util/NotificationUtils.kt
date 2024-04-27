package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.KEY_DOWNLOAD_FILE_NAME
import com.udacity.KEY_DOWNLOAD_STATUS
import com.udacity.R


private const val NOTIFICATION_ID = 0
fun NotificationManager.sendNotification(channelId: String, messageBody: String, applicationContext: Context, fileName: String, status: String) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)

    // put the file name and status
    contentIntent.putExtra(KEY_DOWNLOAD_FILE_NAME, fileName)
    contentIntent.putExtra(KEY_DOWNLOAD_STATUS, status)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        channelId
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}