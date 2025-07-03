package com.mendelu.xmoric.tuttimer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "results")
class Result(
    @ColumnInfo(name = "text") var text: String) : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "name")
    var name: String? = ""

    @ColumnInfo(name = "type")
    var type: String? = null

    @ColumnInfo(name = "minutes")
    var goalMinutes: Int? = null

    @ColumnInfo(name = "seconds")
    var goalSeconds: Int? = null

    @ColumnInfo(name = "goal")
    var goal: String? = null

    @ColumnInfo(name = "metric")
    var metric: String? = null

    @ColumnInfo(name = "time")
    var time: String? = null

    @ColumnInfo(name = "achieved minutes")
    var achievedMinutes: Int? = null

    @ColumnInfo(name = "achieved seconds")
    var achievedSeconds: Int? = null

    @ColumnInfo(name = "achieved")
    var achieved: Boolean? = null

}