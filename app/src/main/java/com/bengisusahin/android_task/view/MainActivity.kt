package com.bengisusahin.android_task.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bengisusahin.android_task.adapter.RecyclerViewAdapter
import com.bengisusahin.android_task.databinding.ActivityMainBinding
import com.bengisusahin.android_task.model.DataModel
import com.bengisusahin.android_task.service.DataModelDB
import com.bengisusahin.android_task.service.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(),NetworkManager.NetworkTaskListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkManager: NetworkManager
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        networkManager = NetworkManager(this)
        networkManager.authorizationRequest()

        //RecyclerView
        recyclerViewAdapter = RecyclerViewAdapter(ArrayList())
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResult(result: List<DataModel>?) {
        result?.let { dataModels ->
            saveDataToRoomDatabase(dataModels)
            runOnUiThread {
                recyclerViewAdapter?.setData(result)
            }
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