package com.bengisusahin.android_task.view

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bengisusahin.android_task.R
import com.bengisusahin.android_task.adapter.RecyclerViewAdapter
import com.bengisusahin.android_task.databinding.ActivityMainBinding
import com.bengisusahin.android_task.model.DataModel
import com.bengisusahin.android_task.service.DataRefreshWorker
import com.bengisusahin.android_task.service.NetworkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(),NetworkManager.NetworkTaskListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkManager: NetworkManager
    private lateinit var scanActivity: ScanActivity
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var dataModels: List<DataModel>

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

        scanActivity = ScanActivity()
        swipeRefreshData()
        startDataRefreshWorker()
    }

    override fun onResult(result: List<DataModel>?) {
        result?.let { dataModels ->
            runOnUiThread {
                recyclerViewAdapter.setData(dataModels)
                this@MainActivity.dataModels = dataModels
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint

        val scannedText = intent.getStringExtra("scanned_text")
        if (!scannedText.isNullOrEmpty()) {
            searchView.setQuery(scannedText, false)
            searchView.post {
                searchView.isIconified = false
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchText ->
                    val filteredList = dataModels.filter { dataModel ->
                        dataModel.title.contains(searchText, ignoreCase = true)
                    }
                    recyclerViewAdapter.setData(filteredList)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchText ->
                    val filteredList = dataModels.filter { dataModel ->
                        dataModel.toString().toLowerCase().contains(searchText.toLowerCase())
                    }
                    recyclerViewAdapter.setData(filteredList)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.qr_scan_scanner -> {
                val intent = Intent(this, ScanActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun swipeRefreshData(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            networkManager.authorizationRequest()
        }
    }

    private fun startDataRefreshWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dataRefreshRequest = PeriodicWorkRequestBuilder<DataRefreshWorker>(60,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "DataRefreshWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            dataRefreshRequest
        )

//        WorkManager.getInstance(this).getWorkInfoByIdLiveData(dataRefreshRequest.id).observe(this,
//            Observer {
//                if (it.state == WorkInfo.State.RUNNING){
//                    println("running")
//                }
//            })
    }

}