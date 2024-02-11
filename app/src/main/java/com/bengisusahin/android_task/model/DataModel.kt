package com.bengisusahin.android_task.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_models")
data class DataModel(
    @ColumnInfo(name = "task")
    val task: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "sort")
    val sort: String,
    @ColumnInfo(name = "wageType")
    val wageType: String,
    @ColumnInfo(name = "BusinessUnitKey")
    val businessUnitKey: String,
    @ColumnInfo(name = "businessUnit")
    val businessUnit: String,
    @ColumnInfo(name = "parentTaskID")
    val parentTaskID: String,
    @ColumnInfo(name = "preplanningBoardQuickSelect")
    val preplanningBoardQuickSelect: String,
    @ColumnInfo(name = "colorCode")
    val colorCode: String?,
    @ColumnInfo(name = "workingTime")
    val workingTime: String,
    @ColumnInfo(name = "isAvailableInTimeTrackingKioskMode")
    val isAvailableInTimeTrackingKioskMode: Boolean
)
{
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}
//example json data
//"task": "10 Aufbau",
//"title": "Gerüst montieren",
//"description": "Gerüste montieren.",
//"sort": "0",
//"wageType": "10 Aufbau",
//"BusinessUnitKey": "Gerüstbau",
//"businessUnit": "Gerüstbau",
//"parentTaskID": "",
//"preplanningBoardQuickSelect": null,
// "colorCode": "#1df70e",
//"workingTime": null,
//"isAvailableInTimeTrackingKioskMode": false
