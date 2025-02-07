package com.bengisusahin.android_task.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bengisusahin.android_task.model.DataModel

@Database(entities = [DataModel::class], version = 1)
abstract class DataModelDB : RoomDatabase() {
    abstract fun dataDao() : DataModelDao

    //Singleton
    companion object{
        @Volatile private var instance : DataModelDB? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext, DataModelDB::class.java, "datamodeldatabase"
        ).build()
    }
}