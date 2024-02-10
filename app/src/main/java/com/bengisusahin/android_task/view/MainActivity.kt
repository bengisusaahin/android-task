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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    //private val networkManager = NetworkManager(listener = )
    private var dataModels: ArrayList<DataModel>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //networkManager.authorizationRequest()

        //RecyclerView
        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        recyclerViewAdapter =
            dataModels?.let { RecyclerViewAdapter(it) } // Assuming the adapter takes a context
        binding.recyclerView.adapter = recyclerViewAdapter
        createDataList()
    }

    private fun createDataList() {
        val db = Room.databaseBuilder(
            applicationContext,
            DataModelDB::class.java, "datamodeldatabase"
        ).build()
        val dataDao = db.dataDao()
        CoroutineScope(Dispatchers.IO).launch {
            // Perform database operations here
            val dataModels = dataDao.getAllDataModels()
            withContext(Dispatchers.Main) {
                // Update UI elements on the main thread
                recyclerViewAdapter?.setData(dataModels)

            }
        }


    }
}