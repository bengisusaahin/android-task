package com.bengisusahin.android_task.service

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bengisusahin.android_task.model.DataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataRefreshWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext,params),NetworkManager.NetworkTaskListener {
    override suspend fun doWork(): Result {
        return try {
            refreshData()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            val networkManager = NetworkManager(this@DataRefreshWorker)
            networkManager.authorizationRequest()
        }
    }
    override fun onResult(result: List<DataModel>?) {
        result?.let { newData ->
            saveDataToRoomDatabase(newData)
        }
    }

    private fun saveDataToRoomDatabase(dataModels: List<DataModel>) {
        val db = Room.databaseBuilder(
            applicationContext,
            DataModelDB::class.java, "datamodeldatabase"
        ).build()
        val dataDao = db.dataDao()

        CoroutineScope(Dispatchers.IO).launch {
            dataDao.insertAll(dataModels)
        }
    }
}