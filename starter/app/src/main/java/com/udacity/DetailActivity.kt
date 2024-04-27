package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()

        // get file name and status
        val fileName = intent.getStringExtra(KEY_DOWNLOAD_FILE_NAME)
        val downloadStatus = intent.getStringExtra(KEY_DOWNLOAD_STATUS)

        binding.contentDetail.tableFileNameValue.text = fileName
        binding.contentDetail.tableStatusValue.text = downloadStatus

        binding.contentDetail.detailActivityConfirmButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
    }
}
