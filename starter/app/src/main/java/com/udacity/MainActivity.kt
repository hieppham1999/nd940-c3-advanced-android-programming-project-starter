package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var selectedOption: AppDownloadOption? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        // TODO: Implement code below

        binding.contentMain.radioGroupApp.setOnCheckedChangeListener { _, checkedId ->
            selectedOption = when (checkedId) {
                binding.contentMain.radioButtonAppGlide.id -> AppDownloadOption.GLIDE
                binding.contentMain.radioButtonAppLoadApp.id -> AppDownloadOption.LOAD_APP
                binding.contentMain.radioButtonAppRetrofit.id -> AppDownloadOption.RETROFIT
                else -> null
            }
        }

        binding.contentMain.customButton.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {

        if (selectedOption != null) {
            val request = DownloadManager.Request(Uri.parse(selectedOption!!.url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.button_download_select_nothing),
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }
}