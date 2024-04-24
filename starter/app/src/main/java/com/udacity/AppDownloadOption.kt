package com.udacity

enum class AppDownloadOption(val url: String, val title: Int) {
    GLIDE(url = "https://github.com/bumptech/glide/archive/refs/heads/master.zip", title = R.string.radio_option_glide),
    LOAD_APP(
        url = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip",
        title = R.string.radio_option_load_app
    ),
    RETROFIT(
        url = "https://github.com/square/retrofit/archive/refs/heads/trunk.zip",
        title = R.string.radio_option_retrofit
    ),
}