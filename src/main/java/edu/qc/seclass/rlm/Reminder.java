package edu.qc.seclass.rlm;

import java.util.Date;

public class Reminder {
    int reminderId = 0;
    String reminderType;
    String reminderName;

    String reminderDate;
    String reminderLocation;
    boolean reminderCheck = false;


    public Reminder(String reminderType , String reminderName , String reminderDate , String reminderLocation) {
        this.reminderType = reminderType;
        this.reminderName = reminderName;
        this.reminderDate = reminderDate;
        this.reminderLocation = reminderLocation;
        this.reminderId++;
    }

    public void toggleReminderCheck(){
        if(reminderCheck == false){
            reminderCheck = true;
        }
        else reminderCheck = false;
    }





}
