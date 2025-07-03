package com.mendelu.xmoric.tuttimer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "activities")
class Activity(
    @ColumnInfo(name = "text") var text: String) : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "name")
    var name: String? = ""

    @ColumnInfo(name = "type")
    var type: String? = null

    @ColumnInfo(name = "minutes")
    var minutes: Int? = null

    @ColumnInfo(name = "seconds")
    var seconds: Int? = null

    @ColumnInfo(name = "goal")
    var goal: String? = null

    @ColumnInfo(name = "metric")
    var metric: String? = null

}