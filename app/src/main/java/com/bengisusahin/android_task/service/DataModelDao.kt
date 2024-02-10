package com.bengisusahin.android_task.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bengisusahin.android_task.model.DataModel

@Dao
interface DataModelDao {
    //Data Access Object
    //Insert into the database
    //suspend -> coroutine, pause & resume
    @Insert
    suspend fun insertAll(dataModels: List<DataModel>)

    @Query("SELECT * FROM data_models")
    fun getAllDataModels() : List<DataModel>

    @Query("SELECT * FROM data_models WHERE uuid = :itemId")
    suspend fun getDataModel(itemId : Int) : DataModel

}