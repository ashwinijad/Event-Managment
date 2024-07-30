package com.example.eventmanagementapp.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
class EventEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String? = "",
    @ColumnInfo(name = "date")
    var date: String? = "",
    @ColumnInfo(name = "time")
    var time: String?= "",
    @ColumnInfo(name = "location")
    var location:String? ="",
    @ColumnInfo(name = "description")
    var description: String? = "",
    @ColumnInfo(name = "participants_list") var participantsList: String = ""
)
