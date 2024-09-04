package edu.qc.seclass.rlm;

import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListManager  {
    ArrayList<ReminderList> listManager;
    public ListManager(){
        listManager = new ArrayList<ReminderList>();
    }

    public void createList(String name){
       ReminderList list = new ReminderList(name);
       listManager.add(list);
       sort();
    }

    public void editList(String name,int listId){
        ReminderList item = new ReminderList(name);
        listManager.set(listId,item);
        item.ListID--;
    }

    public void deleteList(int listId){
        listManager.remove(listId);
    }

    public ReminderList getList(String listName){
        for(int i = 0; i<listManager.size(); i++){
            if(listName == listManager.get(i).ListName){
                return listManager.get(i);
            }
        }

        return null;
    }

    public void sort(){
        Collections.sort(this.listManager, new Comparator<ReminderList>() {
            @Override
            public int compare(ReminderList o1, ReminderList o2) {
                if(o1.ListName == o2.ListName) return 0;
                return o1.ListName.compareTo(o2.ListName ) > 0 ? 1 : -1;
            }
        });
    }


}
