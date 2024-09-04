package edu.qc.seclass.rlm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_names")

data class ListNames(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "list_name") var listName: String?, )
