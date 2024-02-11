package com.bengisusahin.android_task.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bengisusahin.android_task.R
import com.bengisusahin.android_task.adapter.RecyclerViewAdapter
import com.bengisusahin.android_task.databinding.ActivityMainBinding
import com.bengisusahin.android_task.model.DataModel
import com.bengisusahin.android_task.service.DataModelDB
import com.bengisusahin.android_task.service.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),NetworkManager.NetworkTaskListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkManager: NetworkManager
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
    }

    override fun onResult(result: List<DataModel>?) {
        result?.let { dataModels ->
            saveDataToRoomDatabase(dataModels)
            runOnUiThread {
                recyclerViewAdapter?.setData(result)
                this@MainActivity.dataModels = result
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchText ->
                    val filteredList = dataModels.filter { dataModel ->
                        dataModel.title.contains(searchText, ignoreCase = true)
                    }
                    recyclerViewAdapter.filterList(filteredList)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchText ->
                    val filteredList = dataModels.filter { dataModel ->
                        dataModel.toString().toLowerCase().contains(searchText.toLowerCase())
                    }
                    recyclerViewAdapter.filterList(filteredList)
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}