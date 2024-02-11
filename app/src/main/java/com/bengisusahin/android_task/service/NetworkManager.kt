package com.bengisusahin.android_task.service

import com.bengisusahin.android_task.model.DataModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class NetworkManager(private val listener : NetworkTaskListener) {

    private val client = OkHttpClient()

    interface NetworkTaskListener {
        fun onResult(result: List<DataModel>?)
    }
    fun authorizationRequest() {
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
                        //println(result)
                        val dataModels = parseJsonToDataModels(result)
                        listener.onResult(dataModels)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        listener.onResult(null)
                    }
                })
            }
            override fun onFailure(call: Call, e: IOException) {
                listener.onResult(null)
            }
        })
    }

    private fun parseJsonToDataModels(jsonString: String?): List<DataModel>? {
        val dataModels = mutableListOf<DataModel>()
        val jsonArray = JSONArray(jsonString)


        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val dataModel = DataModel(
                jsonObject.getString("task"),
                jsonObject.getString("title"),
                jsonObject.getString("description"),
                jsonObject.getString("sort"),
                jsonObject.getString("wageType"),
                jsonObject.getString("BusinessUnitKey"),
                jsonObject.getString("businessUnit"),
                jsonObject.getString("parentTaskID"),
                jsonObject.getString("preplanningBoardQuickSelect"),
                jsonObject.getString("colorCode"),
                jsonObject.getString("workingTime"),
                jsonObject.getBoolean("isAvailableInTimeTrackingKioskMode")
            )
            dataModels.add(dataModel)
        }
        return dataModels
    }
}