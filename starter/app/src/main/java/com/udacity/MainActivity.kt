package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.util.sendNotification

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private var selectedOption: AppDownloadOption? = null
    private var lastRepoDownload: AppDownloadOption? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name),
        )

        binding.contentMain.radioGroupApp.setOnCheckedChangeListener { _, checkedId ->
            selectedOption = when (checkedId) {
                binding.contentMain.radioButtonAppGlide.id -> AppDownloadOption.GLIDE
                binding.contentMain.radioButtonAppLoadApp.id -> AppDownloadOption.LOAD_APP
                binding.contentMain.radioButtonAppRetrofit.id -> AppDownloadOption.RETROFIT
                else -> null
            }
        }

        binding.contentMain.customButton.setOnClickListener {
            binding.contentMain.customButton.updateState(ButtonState.Clicked)

            if (selectedOption == null) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.button_download_select_nothing),
                    Toast.LENGTH_SHORT
                ).show()
                binding.contentMain.customButton.updateState(ButtonState.Completed)
            } else {
                download()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            // Query the download manager about downloads that have been requested.
            val query = DownloadManager.Query()
            query.setFilterById(id!!)
            val cursor = downloadManager.query(query)
            if (cursor?.moveToFirst() == true) {
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = cursor.getInt(columnIndex)

                val statusString = when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> "Success"
                    else -> "Failed"
                }

                val notificationManager = ContextCompat.getSystemService(
                    applicationContext,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    getString(R.string.download_notification_channel_id),
                    getString(R.string.notification_description),
                    applicationContext,
                    lastRepoDownload?.let { getString(it.title) } ?: "",
                    statusString
                )
            }
        }
    }

    private fun download() {
        selectedOption?.let {
            lastRepoDownload = it

            val notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.cancelAll()

            val request = DownloadManager.Request(Uri.parse(it.url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.

            val query = DownloadManager.Query()
            // set the query filter to our previously Enqueued download
            query.setFilterById(downloadID)
        }
    }



    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Repo Download State"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)


        }
    }
}