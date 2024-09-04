package edu.qc.seclass.rlm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.main_toolbar)

        val db = Room.databaseBuilder(
            applicationContext,
            RLMDatabase::class.java, "new-database"
        ).allowMainThreadQueries().build()

        val rlmDao = db.RLMDao()



        // Initialize data.
        val manager = ListManager()

        initiate(manager,rlmDao)


        displayLists(manager,rlmDao)


        val createListButton: Button = findViewById(R.id.createList)
        val editListButton: Button = findViewById(R.id.editList)
        val deleteListButton: Button = findViewById(R.id.deleteList)
        val searchButton: Button = findViewById(R.id.searchButton)


        createListButton.setOnClickListener(){
            createList(manager,rlmDao)
        }

        editListButton.setOnClickListener(){
            editList(manager,rlmDao)
        }

        deleteListButton.setOnClickListener(){
            deleteList(manager,rlmDao)
        }

        searchButton.setOnClickListener(){
            searchList(manager,rlmDao)
        }

    }

    private fun initiate(manager: ListManager,rlmDao: RLMDao) {
            if(rlmDao.getListNames().size == 0) return
            else {
                val list = rlmDao.getListNames()
                for (i in 0..(list.size - 1)) {
                    manager.createList(list[i].listName)
                }

                if(rlmDao.getReminders().size == 0) return
                else{
                    val list2 = rlmDao.getReminders()
                    for (i in 0..(list.size - 1)) {
                        for (j in i..(list2.size - 1)) {
                            if(list2[j].listMember == list[i].listName){
                                manager.getList(list[i].listName).add(list2[j].reminderType,list2[j].reminderName, list2[j].reminderDate,
                                    list2[j].reminderLocation)
                            }
                        }
                    }
                }
            }


    }

    private fun enterList(manager: ListManager, list: ReminderList,rlmDao: RLMDao){
        setContentView(R.layout.activity_second)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = list.ListName


        val myDataset = list

        val adapter = ReminderAdapter(this,myDataset.reminderlist)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view2)
        recyclerView.adapter =  adapter

        val createReminderButton: Button = findViewById(R.id.createReminder)
        val backToManagerButton: Button = findViewById(R.id.backToManager)
        val completeCheckBoxAll: CheckBox = findViewById(R.id.checkBoxAll)

        val allRemindersChecked = myDataset.reminderlist.all { reminder ->
            reminder.reminderCheck
        }
        println("allremindercheck: " + allRemindersChecked)

        if(myDataset.reminderlist.size == 0){
            completeCheckBoxAll.isChecked = !allRemindersChecked
        }
        else{
            completeCheckBoxAll.isChecked = allRemindersChecked
        }



        createReminderButton.setOnClickListener(){
            createReminder(manager, list,rlmDao)
        }

        backToManagerButton.setOnClickListener(){
            backToManager(manager,rlmDao)
        }

        completeCheckBoxAll.setOnClickListener(){
            println("checkBoxAll pressed")
            checkOffAll(manager,list,rlmDao)
        }

        adapter.setOnClickListener {clickedItem ->
            enterReminder(manager, list, myDataset.reminderlist[clickedItem], clickedItem,rlmDao)
        }
    }

    private fun enterReminder(manager: ListManager, list: ReminderList, reminder: Reminder, index: Int,rlmDao: RLMDao){
        println("Entered Reminder")
        setContentView(R.layout.activity_third)

        val myDataset = list
        val typeText: TextView = findViewById(R.id.typeText)
        val nameText: TextView = findViewById(R.id.nameText)
        val dateText: TextView = findViewById(R.id.dateText)
        val locationText: TextView = findViewById(R.id.locationText)

        typeText.text = reminder.reminderType
        nameText.text = reminder.reminderName
        dateText.text = reminder.reminderDate
        locationText.text = reminder.reminderLocation

        val editReminderButton: Button = findViewById(R.id.editReminder)
        val deleteReminderButton: Button = findViewById(R.id.deleteReminder)
        val backToReminderListButton: Button = findViewById(R.id.backToReminderList)
        val completeCheckBox: CheckBox = findViewById(R.id.checkBox)

        val reminderComplete = reminder.reminderCheck
        completeCheckBox.isChecked = reminderComplete

        editReminderButton.setOnClickListener(){
            editReminder(manager, list, reminder,rlmDao)
        }

        deleteReminderButton.setOnClickListener(){
            deleteReminder(manager, list, index,rlmDao)
        }

        backToReminderListButton.setOnClickListener(){
            enterList(manager, list,rlmDao)
        }

        completeCheckBox.setOnClickListener(){
            setReminderCheck(reminder,rlmDao)
        }


    }


    private fun createList(manager: ListManager,rlmDao: RLMDao){
        setContentView(R.layout.create_list_form_xml)
        val listName : EditText = findViewById(R.id.enter_list_name)
        val submitButton: Button = findViewById(R.id.submitButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)

        submitButton.setOnClickListener {
            val userInput: String = listName.text.toString()

            manager.createList(userInput)
            rlmDao.insert(ListNames(listName = userInput))
            displayLists(manager,rlmDao)
        }

        cancelButton.setOnClickListener {
            displayLists(manager,rlmDao)
        }
    }


    private fun editList(manager: ListManager,rlmDao: RLMDao){
        setContentView(R.layout.edit_list_form)
        val oldListName : EditText = findViewById(R.id.edit_old_list_name)
        val newListName : EditText = findViewById(R.id.edit_new_list_name)
        val submitButton: Button = findViewById(R.id.editButton)
        val cancelButton: Button = findViewById(R.id.editCancelButton)

        submitButton.setOnClickListener {
            val oldList: String = oldListName.text.toString()
            val newList: String = newListName.text.toString()
            for (i in 0..(manager.listManager.size-1)){
                if(oldList == manager.listManager.get(i).ListName)
                    manager.listManager.get(i).ListName = newList
            }

            displayLists(manager, rlmDao)

            val list = rlmDao.getListNames()
            for (i in 0..(list.size - 1)) {
                if(oldList == list[i].listName){
                    rlmDao.delete(list[i])
                }
            }
            rlmDao.insert(ListNames(listName = newList))
        }

        cancelButton.setOnClickListener {
            displayLists(manager,rlmDao)
        }

    }


    private fun deleteList(manager: ListManager,rlmDao: RLMDao){
        setContentView(R.layout.delete_list_form)
        val listName : EditText = findViewById(R.id.delete_list_name)
        val deleteButton: Button = findViewById(R.id.deleteButton)
        val cancelButton: Button = findViewById(R.id.deleteCancelButton)

        deleteButton.setOnClickListener {
            val userInput: String = listName.text.toString()

            var deleteIndex: Int = -1
            for (i in 0..<(manager.listManager.size)){
                if(userInput == manager.listManager[i].ListName)
                    deleteIndex = i
            }
            if(deleteIndex != -1)manager.listManager.removeAt(deleteIndex)

            val list = rlmDao.getListNames()
            val reminderList = rlmDao.getReminders()
            for (i in 0..(list.size - 1)) {
                if(userInput == list[i].listName){
                    for (i in 0..(reminderList.size-1)) {
                        if(userInput == reminderList[i].listMember){
                            rlmDao.deleteReminders(reminderList[i])
                        }
                    }

                    rlmDao.delete(list[i])
                }
            }

            displayLists(manager,rlmDao)
        }

        cancelButton.setOnClickListener {
            displayLists(manager,rlmDao)
        }

    }

    private fun searchList(manager: ListManager,rlmDao: RLMDao){
        setContentView(R.layout.search_form)
        val reminderType : EditText = findViewById(R.id.search_reminder_type)
        val reminderName : EditText = findViewById(R.id.search_reminder_name)
        val searchButton: Button = findViewById(R.id.searchSubmitButton)
        val cancelButton: Button = findViewById(R.id.searchCancelButton)

        searchButton.setOnClickListener {
            val type: String = reminderType.text.toString()
            val name: String = reminderName.text.toString()

            var results_Reminder = ArrayList<Reminder>()
            var results_ReminderList = ArrayList<ReminderList>()
            var results_Indexes = ArrayList<Int>()

            for (i in 0..(manager.listManager.size - 1)) {
                for (j in 0..(manager.listManager[i].reminderlist.size - 1)) {
                    if(manager.listManager[i].reminderlist[j].reminderName == name || manager.listManager[i].reminderlist[j].reminderType == type){
                        results_Reminder.add(manager.listManager[i].reminderlist[j])
                        results_ReminderList.add(manager.listManager[i])
                        results_Indexes.add(j)
                    }
                }
            }

            setContentView(R.layout.search_results)

            val adapter = ReminderAdapter(this,results_Reminder)

            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view3)
            recyclerView.adapter =  adapter

            val backToManagerButton: Button = findViewById(R.id.backToManager)

            backToManagerButton.setOnClickListener(){
                backToManager(manager,rlmDao)
            }

            adapter.setOnClickListener {clickedItem ->
                enterReminder(manager, results_ReminderList[clickedItem], results_Reminder[clickedItem], results_Indexes[clickedItem],rlmDao)
            }


        }


        cancelButton.setOnClickListener {
            displayLists(manager,rlmDao)
        }


    }




    private fun createReminder(manager: ListManager, list: ReminderList,rlmDao: RLMDao){
        setContentView(R.layout.create_reminder_form)
        val reminderType : EditText = findViewById(R.id.enter_reminder_type)
        val reminderName : EditText = findViewById(R.id.enter_reminder_name)
        val reminderDate : EditText = findViewById(R.id.enter_reminder_date)
        val reminderLocation : EditText = findViewById(R.id.enter_reminder_location)
        val submitButton: Button = findViewById(R.id.reminderSubmitButton)
        val cancelButton: Button = findViewById(R.id.reminderCancelButton)


        submitButton.setOnClickListener {
            val type: String = reminderType.text.toString()
            val name: String = reminderName.text.toString()
            val date: String = reminderDate.text.toString()
            val location: String = reminderLocation.text.toString()

            list.add(type,name,date,location)
            rlmDao.insertReminders(Reminders(listMember = list.ListName, reminderType = type, reminderName = name, reminderDate = date, reminderLocation = location ))
            enterList(manager, list,rlmDao)
        }

        cancelButton.setOnClickListener {
            enterList(manager, list,rlmDao)
        }

    }

    private fun editReminder(manager: ListManager, list: ReminderList, reminder: Reminder,rlmDao: RLMDao){
        setContentView(R.layout.create_reminder_form)
        val reminderType : EditText = findViewById(R.id.enter_reminder_type)
        val reminderName : EditText = findViewById(R.id.enter_reminder_name)
        val reminderDate : EditText = findViewById(R.id.enter_reminder_date)
        val reminderLocation : EditText = findViewById(R.id.enter_reminder_location)
        val submitButton: Button = findViewById(R.id.reminderSubmitButton)
        val cancelButton: Button = findViewById(R.id.reminderCancelButton)

        reminderType.setText(reminder.reminderType)
        reminderName.setText(reminder.reminderName)
        reminderDate.setText(reminder.reminderDate)
        reminderLocation.setText(reminder.reminderLocation)


        submitButton.setOnClickListener {
            val type: String = reminderType.text.toString()
            val name: String = reminderName.text.toString()
            val date: String = reminderDate.text.toString()
            val location: String = reminderLocation.text.toString()

            val list2 = rlmDao.getReminders()
            for (i in 0..(list2.size - 1)) {
                if(reminder.reminderName == list2[i].reminderName){
                    rlmDao.deleteReminders(list2[i])
                }
            }

            reminder.reminderType = type
            reminder.reminderName = name
            reminder.reminderDate = date
            reminder.reminderLocation = location


            rlmDao.insertReminders(Reminders(listMember = list.ListName, reminderType = type, reminderName = name, reminderDate = date, reminderLocation = location, reminderCheck = reminder.reminderCheck ))

            enterList(manager, list,rlmDao)
        }

        cancelButton.setOnClickListener {
            enterList(manager, list,rlmDao)
        }

    }


    private fun deleteReminder(manager: ListManager, list: ReminderList, index: Int,rlmDao: RLMDao){

        val list2 = rlmDao.getReminders()
        for (i in 0..(list2.size - 1)) {
            if(list.reminderlist.get(index).reminderName == list2[i].reminderName){
                rlmDao.deleteReminders(list2[i])
            }
        }

        list.reminderlist.removeAt(index)

        enterList(manager,list, rlmDao)
    }

    private fun checkOffAll(manager: ListManager, list: ReminderList, rlmDao: RLMDao){
       println("CheckingOffAll")

        val allRemindersChecked = list.reminderlist.all { reminder ->
            reminder.reminderCheck
        }
        for (reminder in list.reminderlist) {
            reminder.reminderCheck = !allRemindersChecked
            rlmDao.updateReminderCheck(reminder.reminderName, !allRemindersChecked)
        }
    }

    private fun setReminderCheck(reminder: Reminder,rlmDao: RLMDao){

        println("Trying to set reminder check")
        reminder.reminderCheck = !reminder.reminderCheck

        rlmDao.updateReminderCheck(reminder.reminderName, reminder.reminderCheck)
    }

    private fun backToManager(manager: ListManager,rlmDao: RLMDao){
        displayLists(manager,rlmDao)
    }

    private fun backToReminderList(manager: ListManager, list: ReminderList,rlmDao: RLMDao){
        displayList(manager, list,rlmDao)
    }

    private fun displayLists(manager: ListManager,rlmDao: RLMDao){
        setContentView(R.layout.activity_main)
        val myDataset = Datasource().load(manager.listManager)

        val adapter = ReminderListAdapter(this,myDataset)

        adapter.setOnClickListener {clickedItem ->
            enterList(manager, clickedItem,rlmDao)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter =  adapter

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)

        val createListButton: Button = findViewById(R.id.createList)
        val editListButton: Button = findViewById(R.id.editList)
        val deleteListButton: Button = findViewById(R.id.deleteList)
        val searchButton: Button = findViewById(R.id.searchButton)

        createListButton.setOnClickListener(){
            createList(manager,rlmDao)
        }
        editListButton.setOnClickListener(){
            editList(manager,rlmDao)
        }
        deleteListButton.setOnClickListener(){
            deleteList(manager,rlmDao)
        }

        searchButton.setOnClickListener(){
            searchList(manager,rlmDao)
        }

    }

    private fun displayList(manager: ListManager, reminders: ReminderList,rlmDao: RLMDao){
        setContentView(R.layout.activity_second)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = reminders.ListName

        val myDataset = reminders

        val adapter = ReminderAdapter(this,myDataset.reminderlist)
        adapter.setOnClickListener {clickedItem ->
            println("Before enter reminder")
            enterReminder(manager, reminders, myDataset.reminderlist[clickedItem], clickedItem,rlmDao)
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view2)
        recyclerView.adapter =  adapter

        val createReminderButton: Button = findViewById(R.id.createReminder)
        val backToManagerButton: Button = findViewById(R.id.backToManager)

        createReminderButton.setOnClickListener(){
            createReminder(manager, reminders,rlmDao)
        }

        backToManagerButton.setOnClickListener(){
            backToManager(manager,rlmDao)
        }

    }





}


