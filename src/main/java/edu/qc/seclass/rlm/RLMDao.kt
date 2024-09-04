 package edu.qc.seclass.rlm

 import androidx.room.Dao
 import androidx.room.Delete
 import androidx.room.DeleteTable
 import androidx.room.Insert
 import androidx.room.Query
 import androidx.room.Upsert

 @Dao
 interface RLMDao {
     @Insert
      fun insert(listNames: ListNames)

      @Insert
      fun insertReminders(reminders: Reminders)

      @Delete
      fun delete(listNames: ListNames)

      @Query("DELETE FROM list_names")
      fun deleteAllRows()

     @Query("SELECT *  FROM list_names")
      fun getListNames(): List<ListNames>

      @Query("SELECT * FROM reminders")
      fun getReminders(): List<Reminders>

      @Delete
      fun deleteReminders(reminders: Reminders)

      @Query("UPDATE reminders SET reminder_Check = :reminderCheck WHERE reminder_name = :reminderName")
      fun updateReminderCheck(reminderName: String, reminderCheck: Boolean)


 }
