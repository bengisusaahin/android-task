package com.bengisusahin.android_task.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bengisusahin.android_task.R
import com.bengisusahin.android_task.service.NetworkManager

class MainActivity : AppCompatActivity() {

    private val networkManager = NetworkManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //access token : 412a3e956e2f1d7fbdb308411cfb7fb27346ad4d
        //token type : Bearer
        //example json :  {
        //        "task": "10 Aufbau",
        //        "title": "Gerüst montieren",
        //        "description": "Gerüste montieren.",
        //        "sort": "0",
        //        "wageType": "10 Aufbau",
        //        "BusinessUnitKey": "Gerüstbau",
        //        "businessUnit": "Gerüstbau",
        //        "parentTaskID": "",
        //        "preplanningBoardQuickSelect": null,
        //        "colorCode": "#1df70e",
        //        "workingTime": null,
        //        "isAvailableInTimeTrackingKioskMode": false
        //    },
        networkManager.authorizationRequest()
    }
}