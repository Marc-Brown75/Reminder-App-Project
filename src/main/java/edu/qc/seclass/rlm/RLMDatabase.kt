package edu.qc.seclass.rlm

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ListNames::class,Reminders::class], version = 1)
abstract class RLMDatabase : RoomDatabase() {
    abstract fun RLMDao(): RLMDao
}