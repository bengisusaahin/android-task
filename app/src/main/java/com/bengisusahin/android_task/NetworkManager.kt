package com.bengisusahin.android_task

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class NetworkManager {

    private val client = OkHttpClient()

    fun makeLoginRequest() {
        val mediaType = "application/json".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, "{\"username\":\"365\",\"password\":\"1\"}")
        val request = Request.Builder()
            .url("https://api.baubuddy.de/index.php/login")
            .post(body)
            .addHeader("Authorization", "Basic QVBJX0V4cGxvcmVyOjEyMzQ1NmlzQUxhbWVQYXNz")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {


            override fun onResponse(call: Call, response: Response) {

                val responseString = response.body?.string()
                val jsonObject = responseString?.let { JSONObject(it) }
                val oauthObject = jsonObject?.getJSONObject("oauth")
                val token = oauthObject?.getString("access_token")

                val tasksRequest = Request.Builder()
                    .url("https://api.baubuddy.de/dev/index.php/v1/tasks/select")
                    .addHeader("Authorization", "Bearer $token")
                    .build()


                client.newCall(tasksRequest).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        val result = response.body?.string()
                        println(result)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }
                })
            }
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }
        })
    }
}