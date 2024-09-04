package edu.qc.seclass.rlm;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ReminderList {
    ArrayList<Reminder> reminderlist = new ArrayList<Reminder>();
    ArrayList<String> reminderTypes = new ArrayList<String>();


    int ListID = 0;
    String ListName;


    public ReminderList(String name) {
        ListName = name;
        reminderlist = new ArrayList<Reminder>();
        ListID++;

    }

    public void add(String reminderType , String reminderName , String reminderDate , String reminderLocation) {
        if(!doesExist(reminderType)){
            addReminderType(reminderType);
        }


        Reminder item = new Reminder(reminderType,reminderName,reminderDate,reminderLocation);



        reminderlist.add(item);

        groupByType();


    }

    public void delete(int reminderId) {
         reminderlist.remove(reminderId);
    }

    public void edit(int reminderId, String reminderType , String reminderName , String reminderDate , String reminderLocation) {
          Reminder item = new Reminder(reminderType,reminderName,reminderDate,reminderLocation);
          reminderlist.set(reminderId, item);
    }

    public void reminderCompleted(){

    }

    public void checkOff(int reminderId) {
     reminderlist.get(reminderId).toggleReminderCheck();
    }
    public void clearAll() {
        for(Reminder item : reminderlist){
            if(item.reminderCheck == true)
             item.toggleReminderCheck();
        }
    }

    public void groupByType() {
        Collections.sort(this.reminderlist, new Comparator<Reminder>() {
            @Override
            public int compare(Reminder o1, Reminder o2) {
                if(o1.reminderType == o2.reminderType) return 0;
                return o1.reminderType.compareTo(o2.reminderType ) > 0 ? 1 : -1;
            }
        });
    }

    public boolean doesExist(String reminderType) {
       for(String str : reminderTypes){
           if(reminderType == str) return true;
       }

       return false;
    }

    private void addReminderType(String reminderType) {
       reminderTypes.add(reminderType);
    }

    public String getName() {
        return ListName;
    }


}
