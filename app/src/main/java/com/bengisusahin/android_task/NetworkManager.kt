package com.bengisusahin.android_task

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

class NetworkManager {

    private val client = OkHttpClient()

    fun makeLoginRequest() {
        val mediaType = "application/json".toMediaTypeOrNull()
        val request = Request.Builder()
            .url("https://api.baubuddy.de/dev/index.php/v1/tasks/select")
            .get()
            .addHeader("Authorization", "Bearer 90517c072fcf3906841c79e28cc15f0ac27cca28")
            .addHeader("Content-Type", mediaType.toString())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println("Response body: $responseBody")
                // Handle response
            }
        })
    }
}