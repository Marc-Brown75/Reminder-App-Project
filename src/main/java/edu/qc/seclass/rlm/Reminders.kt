package edu.qc.seclass.rlm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")

data class Reminders(
    @PrimaryKey(autoGenerate = true) var reminderId: Int = 0,
    @ColumnInfo(name = "list_member") var listMember: String?,
    @ColumnInfo(name = "reminder_type")var reminderType: String?,
    @ColumnInfo(name = "reminder_name")var reminderName: String?,

    @ColumnInfo(name = "reminder_date") var reminderDate: String?,
    @ColumnInfo(name = "reminder_location")var reminderLocation: String?,
    @ColumnInfo(name = "reminder_Check")var reminderCheck: Boolean = false)
